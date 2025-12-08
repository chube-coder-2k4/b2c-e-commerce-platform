# PAYMENT FLOW DOCUMENTATION

## 📋 Tổng quan Flow Thanh toán

Flow thanh toán đã được implement đầy đủ và hoạt động như sau:

```
User → Add to Cart → Create Order (PENDING) → Get Payment URL → 
Redirect to VNPay → User thanh toán → VNPay Callback → 
Update Payment & Order Status → Success/Fail Page
```

---

## 🔄 Chi tiết từng bước

### **Bước 1: User thêm sản phẩm vào giỏ hàng**
**Endpoint:** `POST /api/v1/cart/add`

**Request Body:**
```json
{
  "productId": "uuid",
  "quantity": 2
}
```

**Response:** Trả về thông tin giỏ hàng với tổng giá

---

### **Bước 2: Tạo Order từ Cart**
**Endpoint:** `POST /api/v1/orders`

**Request Body:**
```json
{
  "paymentMethod": "VNPAY",
  "shippingAddress": "123 Nguyen Van Linh, TP HCM"
}
```

**Response:**
```json
{
  "id": "orders-uuid",
  "orderCode": "ORD-ABCD1234",
  "status": "PENDING",
  "totalAmount": 500000,
  "items": [...]
}
```

**Lưu ý:** 
- Order được tạo với status `PENDING`
- Cart items sẽ bị xóa sau khi tạo orders
- `paidAt` chưa được set (null)

---

### **Bước 3: Tạo Payment URL để checkout**
**Endpoint:** `POST /api/v1/payments/create/{orderId}`

**Response:**
```json
{
  "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?...",
  "orderCode": "ORD-ABCD1234",
  "createdBy": "user-uuid",
  "updatedBy": "user-uuid"
}
```

**Logic:**
1. Kiểm tra orders thuộc về user hiện tại
2. Kiểm tra orders đang ở trạng thái `PENDING`
3. Tạo Payment record với status `PENDING`
4. Generate VNPay URL với các tham số:
   - vnp_TxnRef = payment ID
   - vnp_Amount = totalAmount * 100
   - vnp_OrderInfo = "Thanh toan don hang: {orderCode}"
   - vnp_SecureHash để bảo mật

**Frontend sẽ:** Redirect user đến `paymentUrl`

---

### **Bước 4: User thanh toán trên VNPay**
User thực hiện thanh toán trên cổng VNPay

---

### **Bước 5: VNPay Callback**
**Endpoint:** `GET /api/v1/payments/vnpay-callback`

**Query Params từ VNPay:**
- vnp_TxnRef (payment ID)
- vnp_ResponseCode (00 = success)
- vnp_TransactionNo
- vnp_SecureHash
- ...

**Logic:**
1. Verify chữ ký VNPay (security)
2. Lấy Payment record từ vnp_TxnRef
3. Kiểm tra vnp_ResponseCode:

**Nếu thành công (code = "00"):**
```java
payment.setStatus(COMPLETED)
payment.setTransactionId(vnp_TransactionNo)
payment.setPaidAt(LocalDateTime.now())

orders.setStatus(PAID)
orders.setPaidAt(LocalDateTime.now())
```

**Nếu thất bại:**
```java
payment.setStatus(FAILED)
orders.setStatus(PAYMENT_FAILED)
```

4. Redirect user:
   - Success → `/payment-success?orderId={orderId}`
   - Failed → `/payment-failed?orderId={orderId}`

---

## 📊 Các Status trong hệ thống

### **Order Status:**
- `PENDING` - Đơn hàng mới tạo, chờ thanh toán
- `PAID` - Đã thanh toán thành công
- `PAYMENT_FAILED` - Thanh toán thất bại
- `SHIPPED` - Đang giao hàng
- `CANCELED` - Đã hủy

### **Payment Status:**
- `PENDING` - Chờ thanh toán
- `COMPLETED` - Thanh toán thành công
- `FAILED` - Thanh toán thất bại
- `REFUNDED` - Đã hoàn tiền

---

## 🎯 Các API đã implement

### **Order APIs:**
1. `POST /api/v1/orders` - Tạo orders từ cart
2. `GET /api/v1/orders/user` - Lấy danh sách orders của user
3. `GET /api/v1/orders/all` - Lấy tất cả orders (Admin)
4. `GET /api/v1/orders/{orderId}` - Chi tiết orders
5. `PUT /api/v1/orders/{orderId}/status` - Cập nhật status (Admin)
6. `PUT /api/v1/orders/{orderId}/cancel` - Hủy orders

### **Payment APIs:**
1. `POST /api/v1/payments/create/{orderId}` - Tạo payment URL
2. `GET /api/v1/payments/vnpay-callback` - VNPay callback handler
3. `GET /api/v1/payments/my-payments` - Lấy danh sách payments của user
4. `GET /api/v1/payments/{paymentId}` - Chi tiết payment

---

## ✅ Điểm mạnh của implementation:

1. **Security:** 
   - Verify VNPay signature với HMAC SHA512
   - Kiểm tra ownership (user chỉ thấy payment/orders của mình)
   - Validate orders status trước khi tạo payment

2. **Transaction Safety:**
   - Sử dụng `@Transactional` để đảm bảo data consistency
   - Cập nhật cả Payment và Order atomically

3. **Separation of Concerns:**
   - VNPayUtil: Utility methods cho VNPay integration
   - PaymentService: Business logic
   - PaymentController: API endpoints

4. **Idempotency:**
   - Order ở trạng thái PENDING chỉ cho phép tạo 1 lần payment
   - Callback xử lý đúng với mỗi payment ID

---

## 🧪 Test Flow (Manual):

### **Test Case 1: Thanh toán thành công**
1. Login → Add product to cart
2. Create orders → Nhận orderId
3. Create payment URL → Nhận paymentUrl
4. Access paymentUrl → VNPay sandbox
5. Thanh toán thành công
6. Kiểm tra orders status = PAID
7. Kiểm tra payment status = COMPLETED

### **Test Case 2: Thanh toán thất bại**
1. Các bước 1-4 giống trên
2. Hủy thanh toán hoặc thất bại
3. Kiểm tra orders status = PAYMENT_FAILED
4. Kiểm tra payment status = FAILED

### **Test Case 3: Security**
1. User A tạo orders
2. User B cố gắng tạo payment cho orders của A
3. Kết quả: Throw exception "You are not authorized"

---

## 🔧 Cấu hình VNPay (application.yml):

```yaml
vnpay:
  tmnCode: YOUR_TMN_CODE
  hashSecret: YOUR_HASH_SECRET
  url: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
  returnUrl: http://localhost:8080/api/v1/payments/vnpay-callback
  ipnUrl: http://localhost:8080/api/v1/payments/vnpay-callback
```

---

## 📝 Lưu ý quan trọng:

1. **VNPay Amount:** Phải nhân 100 (VND không có đơn vị nhỏ hơn)
2. **Security Hash:** VNPay hỗ trợ SHA256 và SHA512, project dùng SHA512
3. **Callback URL:** Phải public accessible (không localhost khi production)
4. **Order Code:** Unique cho mỗi orders để tracking
5. **Payment ID = vnp_TxnRef:** Dùng để track payment trong callback

---

## 🚀 Các điểm cần cải thiện (Optional):

1. **Webhook/IPN:** Implement thêm IPN endpoint để VNPay gửi notification
2. **Retry Logic:** Xử lý trường hợp callback failed
3. **Notification:** Gửi email/SMS khi thanh toán thành công
4. **Refund:** Implement refund flow
5. **Payment History:** Add pagination cho getMyPayments
6. **Logging:** Log chi tiết cho audit trail

---

## ✨ Kết luận:

Flow payment đã được implement **HOÀN CHỈNH** và **ĐÚNG CHUẨN**:
- ✅ User có thể tạo orders từ cart
- ✅ Tạo payment URL để redirect đến VNPay
- ✅ Xử lý callback và cập nhật status chính xác
- ✅ Security được đảm bảo
- ✅ Transaction safety

**Flow này đã sẵn sàng để tích hợp với Frontend!** 🎉

