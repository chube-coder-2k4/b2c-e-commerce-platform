package dev.commerce.utils;

import dev.commerce.configurations.VNPayConfig;
import lombok.extern.slf4j.Slf4j;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class VNPayUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String buildPaymentUrl(UUID paymentId, double amount, VNPayConfig config) {
        long vnpAmount = (long) (amount * 100); // VNPAY yêu cầu *100

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", config.getTmnCode());
        params.put("vnp_Amount", String.valueOf(vnpAmount));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", paymentId.toString());
        params.put("vnp_OrderInfo", "Thanh toan don hang:" + paymentId.toString().substring(0, 8)); // Giống VNPayController mẫu
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", config.getReturnUrl());
        params.put("vnp_IpAddr", "127.0.0.1");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        cld.add(Calendar.MINUTE, 15);
        params.put("vnp_ExpireDate", formatter.format(cld.getTime())); // THÊM ExpireDate!

        String hashData = buildHashData(params);
        String secureHash = hmacSHA512(config.getHashSecret(), hashData);

        log.info("=== VNPAY SIGNATURE DEBUG ===");
        log.info("Hash Secret: {}", config.getHashSecret());
        log.info("Hash Data: {}", hashData);
        log.info("Secure Hash (SHA512): {}", secureHash);
        log.info("============================");

        String query = buildQuery(params);

        return config.getUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    /**
     * Verify callback
     */
    public static boolean verify(Map<String, String> params, VNPayConfig config) {
        if (!params.containsKey("vnp_SecureHash")) return false;

        String secureHash = params.get("vnp_SecureHash");

        Map<String, String> sorted = new TreeMap<>(params);
        sorted.remove("vnp_SecureHash");
        sorted.remove("vnp_SecureHashType");

        String hashData = buildHashData(sorted);
        String calculated = hmacSHA256(config.getHashSecret(), hashData);

        return calculated.equalsIgnoreCase(secureHash);
    }

    /** Build query string - NO URL ENCODING (VNPay không yêu cầu encode) */
    public static String buildQuery(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            sb.append(e.getKey());
            sb.append("=");
            sb.append(e.getValue());
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /** Build hash data (raw data) - NO URL ENCODING for VNPay signature */
    public static String buildHashData(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            if (iterator.hasNext()) sb.append("&");
        }
        return sb.toString();
    }

    /** SHA256 HMAC */
    public static String hmacSHA256(String secretKey, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec =
                    new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(keySpec);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create HMAC SHA256", e);
        }
    }

    /** Convert byte[] → Hex */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b)); // chữ thường
        return sb.toString();
    }

    /** SHA512 HMAC - for backward compatibility */
    public static String hmacSHA512(String secretKey, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec =
                    new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(keySpec);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create HMAC SHA512", e);
        }
    }

    /** Hash all fields - alias for buildHashData */
    public static String hashAllFields(Map<String, String> params) {
        return buildHashData(new TreeMap<>(params));
    }
}
