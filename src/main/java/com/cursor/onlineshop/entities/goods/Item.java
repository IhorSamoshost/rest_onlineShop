package com.cursor.onlineshop.entities.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

// DB Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item")
public class Item {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String itemId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private String price;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String categoryId;

    @Column(name = "amount_in_stock")
    private int amountInStock;

    public Item(String name, String description, String price, int amountInStock) {
        this.itemId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.amountInStock = amountInStock;
    }
}
