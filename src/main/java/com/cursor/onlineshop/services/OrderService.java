package com.cursor.onlineshop.services;

import com.cursor.onlineshop.dtos.CreateOrderDto;
import com.cursor.onlineshop.dtos.CreateOrderItemDto;
import com.cursor.onlineshop.dtos.OrderDto;
import com.cursor.onlineshop.dtos.OrderItemDto;
import com.cursor.onlineshop.entities.orders.Order;
import com.cursor.onlineshop.entities.orders.OrderItem;
import com.cursor.onlineshop.repositories.OrderRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class OrderService {
    private final OrderRepo orderRepo;
    private final UserService userService;
    private final ItemService itemService;


    public Order add(CreateOrderDto newOrderDto) {
        Order newOrder = new Order();
        newOrder.setOrderId(UUID.randomUUID().toString());
        newOrder.setUser(userService.getUserByAccount(userService
                .getByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));
        newOrder.setOrderDate(new Date());
        newOrder.setOrderItems(new HashSet<>());
        for (CreateOrderItemDto coidto : newOrderDto.getOrderItemsDtos()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(UUID.randomUUID().toString());
            orderItem.setItem(itemService.getById(coidto.getItemId()));
            orderItem.setQuantity(coidto.getQuantity());
            orderItem.setPrice(orderItem.getItem().getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            newOrder.addOrderItem(orderItem);
        }
        return orderRepo.save(newOrder);
    }

    public Order update(OrderDto updatedOrderDto) throws ParseException {
        Order updatedOrder = new Order();
        updatedOrder.setOrderId(updatedOrderDto.getOrderId());
        updatedOrder.setUser(userService.getUserByAccount(userService
                .getAccountById(updatedOrderDto.getAccountId())));
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        updatedOrder.setOrderDate(format.parse(updatedOrderDto.getOrderDate()));
        updatedOrder.setOrderItems(new HashSet<>());
        for (OrderItemDto oidto : updatedOrderDto.getOrderItemsDtos()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(oidto.getOrderItemId());
            orderItem.setItem(itemService.getById(oidto.getItemId()));
            orderItem.setQuantity(oidto.getQuantity());
            orderItem.setPrice(orderItem.getItem().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            updatedOrder.addOrderItem(orderItem);
        }
        return orderRepo.save(updatedOrder);
    }

    public String delete(String deleteOrderId) {
        Order orderToDelete = orderRepo.getOne(deleteOrderId);
        orderRepo.delete(orderToDelete);
        return orderRepo.getOne(deleteOrderId) == null ?
                String.format("Order with id=%s succesfully deleted", deleteOrderId) :
                String.format("Order with id=%s not deleted", deleteOrderId);
    }

    public Order getById(String orderId) {
        return orderRepo.getOne(orderId);
    }

    public List<Order> getAll() {
        return orderRepo.findAll();
    }
}
