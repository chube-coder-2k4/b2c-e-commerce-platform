# VNPay Payment Integration - Troubleshooting Guide

## Current Issue: "Sai chữ ký" (Invalid Signature)

### Latest Test Results:
```
Hash Secret: NQMX8QDF8YSFZ729AIO2RWWZK66GXTED
Hash Data: vnp_Amount=5000000&vnp_Command=pay&vnp_CreateDate=20251208152525&vnp_CurrCode=VND&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=Payment for order a0016239-24c8-4130-ae8f-c24d995c56c1&vnp_OrderType=other&vnp_ReturnUrl=http://localhost:8085/api/v1/payments/vnpay-callback&vnp_TmnCode=C68WRIY5&vnp_TxnRef=a0016239-24c8-4130-ae8f-c24d995c56c1&vnp_Version=2.1.0
Secure Hash: 943ff51a4bd63f9c23c5e2be69bf55c10ab803cd83ff577ca01f4aa44a67605f
```

## VNPay Requirements (According to Official Docs):

### Signature Algorithm:
1. **Sort all parameters alphabetically** by key ✅ (using TreeMap)
2. **Build hash data** as: `key1=value1&key2=value2&...` (NO URL encoding) ✅
3. **Calculate HMAC SHA256** with hash secret ✅
4. **Convert to lowercase hex** ✅

### Query String Format:
- According to VNPay docs, query string should NOT be URL encoded
- All values should be raw strings

## Test with VNPay Sandbox Tool:

You can verify the signature manually:
1. Go to: https://sandbox.vnpayment.vn/apis/docs/huong-dan-tich-hop/#tao-url-thanh-toan
2. Use their signature calculator
3. Input same params and hash secret
4. Compare result

## Expected vs Actual:

### Our Hash Data:
```
vnp_Amount=5000000&vnp_Command=pay&vnp_CreateDate=20251208152525&vnp_CurrCode=VND&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=Payment for order a0016239-24c8-4130-ae8f-c24d995c56c1&vnp_OrderType=other&vnp_ReturnUrl=http://localhost:8085/api/v1/payments/vnpay-callback&vnp_TmnCode=C68WRIY5&vnp_TxnRef=a0016239-24c8-4130-ae8f-c24d995c56c1&vnp_Version=2.1.0
```

### Our Hash:
```
943ff51a4bd63f9c23c5e2be69bf55c10ab803cd83ff577ca01f4aa44a67605f
```

## Next Steps:

1. **Restart server** to apply latest changes (removed URL encoding)
2. **Create new payment** (old payments won't work)
3. **Check if it works**

If still fails, possible causes:
- Hash secret key mismatch (check VNPay dashboard)
- Space or special character in vnp_OrderInfo
- Date/time format issue
- Missing required parameters

