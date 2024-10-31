package com.example.userservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactUpdateRequest {
    private String isRead;
    private String isImportant;
    private String isSpam;
}
