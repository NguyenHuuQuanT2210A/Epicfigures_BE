package com.example.userservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;
    private String note;
    private boolean isRead;
    private boolean isImportant;
    private boolean isSpam;
    private Long contactReplyId;
}
