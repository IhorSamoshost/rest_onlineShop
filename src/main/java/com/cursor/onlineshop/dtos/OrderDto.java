package com.cursor.onlineshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDto {
    private String orderId;
    private String accountId;
    private String orderDate;
    private Set<OrderItemDto> orderItemsDtos;
}