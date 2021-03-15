package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CreateAccountDto;
import com.cursor.onlineshop.security.JwtUtils;
import com.cursor.onlineshop.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtTokenUtil;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest auth) {
        return authenticateAndGetJwtCookie(auth.getUsername(), auth.getPassword());
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> createUserAccount(@RequestBody CreateAccountDto createAccountDto) {
        try {
            final UserDetails newAccount = userService.registerUser(createAccountDto);
            return authenticateAndGetJwtCookie(newAccount.getUsername(), createAccountDto.getPassword());
        } catch (AccessDeniedException ade) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized!", ade);
        }
    }

    @PostMapping(value = "/regadmin")
    public ResponseEntity<String> createAdminAccount(@RequestBody CreateAccountDto createAccountDto) {
        try {
            final UserDetails newAccount = userService.registerWithRole(createAccountDto);
            return authenticateAndGetJwtCookie(newAccount.getUsername(), createAccountDto.getPassword());
        } catch (AccessDeniedException ade) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized!", ade);
        }
    }

    public ResponseEntity<String> authenticateAndGetJwtCookie(String userName, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userName, password));
            UserDetails userDetails = userService.login(userName, password);
            String jwt = jwtTokenUtil.generateToken(userDetails);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "jwt=" + jwt);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
        } catch (AccessDeniedException ade) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized!", ade);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationRequest {
        private String username;
        private String password;
    }
}