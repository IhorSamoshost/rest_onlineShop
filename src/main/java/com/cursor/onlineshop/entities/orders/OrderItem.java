package com.cursor.onlineshop.entities.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

// DB Entity
@Entity
@Table(name = "order_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @ManyToOne
    @JoinColumn(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderItemId;

    @OneToOne
    @JoinColumn(name = "item_id")
    private String itemId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private BigDecimal price;

    public OrderItem(String orderItemId) {
        this.orderItemId = orderItemId;
    }
}
