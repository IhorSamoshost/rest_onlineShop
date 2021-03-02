package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.UserDto;
import com.cursor.onlineshop.entities.user.Account;
import com.cursor.onlineshop.entities.user.UserPermission;
import com.cursor.onlineshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDto>> getAllUsersInfo() {
        List<UserDto> usersDtos = userService.getAll();
        return usersDtos != null
                ? new ResponseEntity<>(usersDtos, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{accountId}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable(value = "accountId") String accountId) {
        UserDto requestedUserDto = userService.getUserInfoById(accountId);
        String requestedUserName = requestedUserDto.getUsername();
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedUserName)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return new ResponseEntity<>(requestedUserDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping(
            value = "/{accountId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> editUser(@PathVariable(value = "accountId") String accountId,
                                            @RequestBody UserDto editedUserDto) {
        Account accountToEdit = userService.getAccountById(accountId);
        String accountToEditUsername = accountToEdit.getUsername();
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(accountToEditUsername)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return new ResponseEntity<>(userService.update(editedUserDto), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
