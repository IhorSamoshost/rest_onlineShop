package com.cursor.onlineshop.dtos;

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

    private User toEntity() {
        return new User(accountId, firstName, lastName, age, phoneNumber);
    }
}
