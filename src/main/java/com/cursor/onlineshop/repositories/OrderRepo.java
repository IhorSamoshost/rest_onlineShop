package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, String> {
}
