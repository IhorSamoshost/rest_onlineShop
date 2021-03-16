package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CreateOrderDto;
import com.cursor.onlineshop.dtos.OrderDto;
import com.cursor.onlineshop.entities.orders.Order;
import com.cursor.onlineshop.entities.user.Account;
import com.cursor.onlineshop.entities.user.UserPermission;
import com.cursor.onlineshop.services.OrderService;
import com.cursor.onlineshop.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order requestedOrder = orderService.getById(orderId);
        String requestedOrderUserName = userService.getAccountById(requestedOrder.getUser().getAccountId()).getUsername();
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedOrderUserName) || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return ResponseEntity.ok(requestedOrder);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDto newOrderDto) {
        return ResponseEntity.ok(orderService.add(newOrderDto));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> editOrder(@PathVariable String orderId, @RequestBody OrderDto updatedOrderDto)
            throws ParseException {
        updatedOrderDto.setOrderId(orderId);
        Order requestedOrder = orderService.getById(orderId);
        String requestedOrderUserName = userService
                .getAccountById(requestedOrder.getUser().getAccountId()).getUsername();
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedOrderUserName)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return ResponseEntity.ok(orderService.update(updatedOrderDto));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
        Order requestedOrder = orderService.getById(orderId);
        String requestedOrderUserName = userService
                .getAccountById(requestedOrder.getUser().getAccountId()).getUsername();
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedOrderUserName)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return ResponseEntity.ok(orderService.delete(orderId));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/exec/{orderId}")
    public ResponseEntity<String> executeOrder(@PathVariable String orderId) {
        Order requestedOrder = orderService.getById(orderId);
        String requestedOrderUserName = userService
                .getAccountById(requestedOrder.getUser().getAccountId()).getUsername();
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedOrderUserName)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return ResponseEntity.ok(orderService.exec(orderId));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
