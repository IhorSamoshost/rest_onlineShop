package com.cursor.onlineshop.dtos;

import com.cursor.onlineshop.entities.goods.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDto {
    private String name;
    private String description;
    private double price;
    private int amountInStock;
    private String categoryId;

    public Item toEntity() {
        return new Item(name, description, BigDecimal.valueOf(price), amountInStock, categoryId);
    }
}
