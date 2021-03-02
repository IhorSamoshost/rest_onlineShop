package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CreateOrderDto;
import com.cursor.onlineshop.dtos.OrderDto;
import com.cursor.onlineshop.entities.orders.Order;
import com.cursor.onlineshop.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable String orderId) {
        return orderService.getById(orderId);
    }

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderDto newOrderDto) {
        return orderService.add(newOrderDto);
    }

    @PutMapping("/{orderId}")
    public Order editOrder(@PathVariable String orderId, @RequestBody OrderDto updatedOrderDto)
            throws ParseException {
        updatedOrderDto.setOrderId(orderId);
        return orderService.update(updatedOrderDto);
    }

    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable String orderId) {
        return orderService.delete(orderId);
    }
}
