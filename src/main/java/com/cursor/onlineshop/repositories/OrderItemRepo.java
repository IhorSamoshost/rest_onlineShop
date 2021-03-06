package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.orders.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {
}
