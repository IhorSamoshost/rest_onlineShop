package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.goods.Category;
import com.cursor.onlineshop.entities.orders.Order;
import com.cursor.onlineshop.entities.orders.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {
    Optional<OrderItem> findByOrderItemId(String orderItemId);
}
