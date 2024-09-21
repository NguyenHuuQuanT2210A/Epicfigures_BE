package com.example.userservice.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Statistics {
    private int totalUsersAdmin;
    private int totalUsersCustomer;
}
