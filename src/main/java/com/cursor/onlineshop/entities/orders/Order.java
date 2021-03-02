package com.cursor.onlineshop.entities.orders;

import com.cursor.onlineshop.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    private String orderId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private User user;
    @Column(name = "date")
    private Date orderDate;
    @Column(name = "order_price")
    private BigDecimal orderPrice;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<OrderItem> orderItems;

    public void addOrderItem(OrderItem orderItem) {
       orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }
}
