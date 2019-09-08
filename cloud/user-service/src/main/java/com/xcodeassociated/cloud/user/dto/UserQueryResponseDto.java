package com.xcodeassociated.cloud.user.dto;

import com.xcodeassociated.cloud.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserQueryResponseDto {
    private String username;
    private String password;
    private UserRole role;
}
