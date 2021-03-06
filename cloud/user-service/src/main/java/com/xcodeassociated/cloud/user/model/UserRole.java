package com.xcodeassociated.cloud.user.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserRole {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public static UserRole fromValue(String value) {
        for (UserRole category : values()) {
            if (category.role.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException(
            "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
    }

}
