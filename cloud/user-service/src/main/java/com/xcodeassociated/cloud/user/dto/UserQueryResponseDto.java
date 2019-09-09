package com.xcodeassociated.cloud.user.dto;

import com.xcodeassociated.cloud.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserQueryResponseDto {
    private Long id;
    private String username;
    private String password;
    private List<UserRole> roles;
}
