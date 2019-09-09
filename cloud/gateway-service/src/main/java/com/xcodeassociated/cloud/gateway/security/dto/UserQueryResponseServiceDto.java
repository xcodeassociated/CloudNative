package com.xcodeassociated.cloud.gateway.security.dto;

import com.xcodeassociated.cloud.gateway.security.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserQueryResponseServiceDto {
    private Long id;
    private String username;
    private String password;
    private List<UserRole> roles;
}
