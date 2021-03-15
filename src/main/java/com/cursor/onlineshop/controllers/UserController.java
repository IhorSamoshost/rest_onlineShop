package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.UserDto;
import com.cursor.onlineshop.entities.user.Account;
import com.cursor.onlineshop.entities.user.UserPermission;
import com.cursor.onlineshop.security.JwtUtils;
import com.cursor.onlineshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtTokenUtil;

    /**
     * GET - get a list of users sorted by last names and then by first names.
     *
     * @param limitString  a number of returned entities
     * @param offsetString offset
     * @return ResponseEntity with a list of users and HttpStatus
     */
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDto>> getAllUsersInfo(
            @RequestParam(value = "limit", defaultValue = "3", required = false) String limitString,
            @RequestParam(value = "offset", defaultValue = "0", required = false) String offsetString
    ) {
        int limit = Integer.parseInt(limitString);
        int offset = Integer.parseInt(offsetString);
        return ResponseEntity.ok(userService.getAll(limit, offset));
    }

    @GetMapping(value = "/{accountId}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable(value = "accountId") String accountId) {
        UserDto requestedUserDto = userService.getUserInfoById(accountId);
        String requestedUserName = requestedUserDto.getUsername();
        return checkIfRequesterIsRequestedUserOrHasRoleAdmin(requestedUserName) ?
                ResponseEntity.ok(requestedUserDto) : new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping(value = "/{accountId}")
    public ResponseEntity<String> editUser(
            @PathVariable(value = "accountId") String accountId,
            @RequestBody UserDto editedUserDto
    ) {
        UserDto requestedUserDto = userService.getUserInfoById(accountId);
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedUserDto.getUsername())) {
            UserDetails updatedAccount = userService.update(accountId, editedUserDto, requestedUserDto);
            String jwt = jwtTokenUtil.generateToken(updatedAccount);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "jwt=" + jwt);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
        }
        if (requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            userService.update(accountId, editedUserDto, requestedUserDto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping(value = "/{accountId}")
    public ResponseEntity<String> delete(@PathVariable(value = "accountId") String accountId) {
        String requestedUserName = userService.getAccountById(accountId).getUsername();
        if (checkIfRequesterIsRequestedUserOrHasRoleAdmin(requestedUserName)) {
            userService.delete(accountId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private boolean checkIfRequesterIsRequestedUserOrHasRoleAdmin(String requestedUserName) {
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return requester.getUsername().equals(requestedUserName)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN);
    }
}
