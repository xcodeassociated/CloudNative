package com.xcodeassociated.cloud.gateway.security.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserQueryRequestDto {
    private String username;
    private String password;
}
