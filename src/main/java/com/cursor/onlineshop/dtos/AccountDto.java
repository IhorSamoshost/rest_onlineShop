package com.cursor.onlineshop.dtos;

import com.cursor.onlineshop.entities.user.Account;
import com.cursor.onlineshop.entities.user.UserPermission;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class AccountDto {
    private String accountId;
    private String username;
    private String password;
    private String email;
    private Set<UserPermission> permissions;

    private Account toEntity() {
        return new Account(accountId, username, password, email, permissions);
    }
}