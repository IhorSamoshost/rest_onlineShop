package com.cursor.onlineshop.entities.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

// DB Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order")
public class Order implements OrderColumn {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String accountId;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "order_price")
    private BigDecimal orderPrice;

    @Enumerated(EnumType.STRING)
    @OneToOne(mappedBy = "order")
    private Set<OrderItem> orderItems;

    public Order(Date orderDate, BigDecimal orderPrice, Set<OrderItem> orderItems) {
        this.orderId =  UUID.randomUUID().toString();
        this.accountId = UUID.randomUUID().toString();
        this.orderDate = orderDate;
        this.orderPrice = orderPrice;
        this.orderItems = orderItems;
    }

    @Override
    public String name() {
        return orderId;
    }

    @Override
    public boolean nullable() {
        return true;
    }

    @Override
    public boolean insertable() {
        return true;
    }

    @Override
    public boolean updatable() {
        return true;
    }

    @Override
    public String columnDefinition() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
