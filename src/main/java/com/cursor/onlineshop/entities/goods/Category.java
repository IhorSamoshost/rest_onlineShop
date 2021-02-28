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
@Table(name = "category")
public class Category {

    @Id
    @OneToOne
    @JoinColumn(name = "category_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String categoryId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    public Category(String name, String description, String image ) {
        this.categoryId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
