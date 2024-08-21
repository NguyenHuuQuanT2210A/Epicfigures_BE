package com.example.orderservice.specification;

import com.example.common.enums.OrderSimpleStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchBody {
    private int page;
    private int limit;
    private String timeSorting;
    private Integer status;
    private String priceSorting;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String startDate;
    private String endDate;
    private String productName;
    private String orderId;

}
