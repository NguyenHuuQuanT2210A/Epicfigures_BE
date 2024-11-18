package com.example.userservice.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest {
    private String username;
    private String phoneNumber;
    private String email;
    private String note;
    private Long contactReplyId;
}
