package com.cursor.onlineshop.entities.orders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

// DB Entity
public class Order {

    private String orderId;
    private String accountId;
    private Date orderDate;
    private BigDecimal orderPrice;
    private Set<OrderItem> orderItems;
}
