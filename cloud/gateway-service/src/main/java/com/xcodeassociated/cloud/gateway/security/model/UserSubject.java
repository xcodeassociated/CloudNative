package com.xcodeassociated.cloud.gateway.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubject {
    private Long id;
    private String name;
}
