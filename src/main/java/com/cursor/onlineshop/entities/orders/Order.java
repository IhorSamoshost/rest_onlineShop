package com.cursor.onlineshop.entities.orders;

import com.cursor.onlineshop.entities.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    private String orderId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @Column(name = "date")
    private Date orderDate;
    @Column(name = "order_price")
    private BigDecimal orderPrice;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<OrderItem> orderItems;

    public Order() {
        orderItems = new HashSet<>();
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", user={" + user.getAccountId() + " " + user.getFirstName() + " " + user.getLastName() + "}" +
                ", orderDate=" + orderDate +
                ", orderPrice=" + orderPrice +
                ", orderItems=" + orderItems +
                '}';
    }
}
