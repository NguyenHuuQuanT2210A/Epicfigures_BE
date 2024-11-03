package com.example.orderservice.dto.request;

import com.example.orderservice.enums.ReturnItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnItemStatusRequest {
    private ReturnItemStatus status;
    private String statusNote;
}
