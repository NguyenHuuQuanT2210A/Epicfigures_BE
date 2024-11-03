package com.example.orderservice.enums;

public enum ReasonReturnItemStatus {
    DAMAGED_DURING_SHIPPING,         // Hư hỏng trong quá trình vận chuyển
    MISSING_PARTS_OR_ACCESSORIES,     // Thiếu bộ phận hoặc phụ kiện
    INCORRECT_MODEL_OR_ITEM_SENT,     // Gửi sai mô hình hoặc sản phẩm
    PAINT_DEFECTS_OR_IMPERFECTIONS,   // Lỗi sơn hoặc chi tiết không hoàn thiện
    SIZE_OR_SCALE_ISSUE,              // Vấn đề về kích thước hoặc tỷ lệ
    QUALITY_NOT_AS_EXPECTED,          // Chất lượng không như mong đợi
    RECEIVED_COUNTERFEIT_OR_KNOCKOFF, // Nhận phải hàng giả hoặc nhái
    CHANGED_MIND,                     // Thay đổi ý định mua hàng
    BETTER_DEAL_FOUND,                // Tìm thấy giá tốt hơn
    INCOMPLETE_SET_OR_LIMITED_EDITION_ERROR, // Bộ không đầy đủ hoặc lỗi phiên bản giới hạn
    OTHER,                             // Khác
}
