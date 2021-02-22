package com.cursor.onlineshop.dtos;

import com.cursor.onlineshop.entities.user.Account;
import com.cursor.onlineshop.entities.user.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {
    private String username;
    private String password;
    private String email;
    private Set<UserPermission> permissions;

    private Account toEntity() {
        return new Account(username, password, email);
    }

    private Account toEntityWithRole() {
        return new Account(username, password, email, permissions);
    }
}
