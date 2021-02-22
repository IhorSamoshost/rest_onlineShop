package com.cursor.onlineshop.dtos;

import com.cursor.onlineshop.entities.user.Account;
import com.cursor.onlineshop.entities.user.User;
import com.cursor.onlineshop.entities.user.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserDto {
    private String accountId;
    private String username;
    private String password;
    private String email;
    private Set<UserPermission> permissions;
    private String firstName;
    private String lastName;
    private Integer age;
    private String phoneNumber;

    public UserDto(String username, String password, String email, Set<UserPermission> permissions,
                   String firstName, String lastName, Integer age, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.permissions = permissions;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    private User toEntity() {
        return new User(accountId, firstName, lastName, age, phoneNumber);
    }
}
