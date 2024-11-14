package com.example.orderservice.enums;

public enum ReturnItemStatus {
    PENDING,       // Yêu cầu trả hàng vừa được gửi, đang chờ duyệt
    APPROVED,      // Yêu cầu đã được duyệt, người dùng có thể gửi hàng về
    REJECTED,      // Yêu cầu bị từ chối, không được phép trả hàng
//    IN_TRANSIT,    // Hàng đang được vận chuyển từ người dùng về
//    INSPECTION,    // Hàng đã nhận được và đang kiểm tra tình trạng
    REFUNDED,      // Hoàn tiền đã được xử lý xong cho yêu cầu trả hàng
    REPLACEMENT_SHIPPED, // Sản phẩm thay thế đã được gửi đến người dùng
//    COMPLETED,      // Quy trình trả hàng hoàn tất, không còn hành động nào nữa
}
