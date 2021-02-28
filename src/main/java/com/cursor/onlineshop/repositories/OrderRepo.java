package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.goods.Category;
import com.cursor.onlineshop.entities.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, String > {
    Optional<Order> findByOrderId(String orderId);
}
