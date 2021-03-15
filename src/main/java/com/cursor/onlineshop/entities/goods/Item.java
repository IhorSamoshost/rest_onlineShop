package com.cursor.onlineshop.entities.goods;

import com.cursor.onlineshop.exceptions.InvalidSortValueException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "item_id")
    private String itemId;
    @Column(name = "item_name")
    private String name;
    @Column(name = "item_description")
    private String description;
    private BigDecimal price;
    @Column(name = "amount_in_stock")
    private int amountInStock;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Category category;

    public Item(String name, String description, BigDecimal price, int amountInStock, Category category) {
        this.itemId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.amountInStock = amountInStock;
        this.category = category;
    }

    public enum Sort {

        NAME("name"),
        CATEGORY("category"),
        PRICE_ASC("price_asc"),
        PRICE_DESC("price_desc");

        private final String value;

        Sort(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Sort determineSortType(final String stringValue) throws InvalidSortValueException {
            for (Sort sortObject : values()) {
                if (stringValue.equals(sortObject.getValue())) {
                    return sortObject;
                }
            }
            throw new InvalidSortValueException();
        }
    }
}
