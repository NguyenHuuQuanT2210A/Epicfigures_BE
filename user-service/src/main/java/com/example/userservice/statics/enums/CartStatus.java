package com.example.userservice.statics.enums;

public enum CartStatus {
    AVAILABLE,       // Sản phẩm có sẵn và có thể thanh toán
    OUT_OF_STOCK,    // Sản phẩm hết hàng
    DISCONTINUED,    // Sản phẩm ngừng kinh doanh
    EXPIRED,         // Giỏ hàng hoặc sản phẩm đã hết hạn
    PRICE_CHANGED,   // Giá của sản phẩm đã thay đổi
    UNAVAILABLE      // Sản phẩm không khả dụng vì lý do khác
}
