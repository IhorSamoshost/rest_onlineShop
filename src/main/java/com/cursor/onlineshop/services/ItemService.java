package com.cursor.onlineshop.services;

import com.cursor.onlineshop.dtos.CreateItemDto;
import com.cursor.onlineshop.dtos.ItemDto;
import com.cursor.onlineshop.entities.goods.Item;
import com.cursor.onlineshop.exceptions.InvalidSortValueException;
import com.cursor.onlineshop.repositories.ByCriteriaRepo;
import com.cursor.onlineshop.repositories.ItemRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class ItemService {

    private final ItemRepo itemRepo;
    private final ByCriteriaRepo byCriteriaRepo;
    private final CategoryService categoryService;

    @Transactional
    public Item add(CreateItemDto newItemDto) {
        Item newItem = new Item(newItemDto.getName(), newItemDto.getDescription(),
                BigDecimal.valueOf(newItemDto.getPrice()), newItemDto.getAmountInStock(),
                categoryService.getById(newItemDto.getCategoryId()));
        return itemRepo.save(newItem);
    }

    public Item update(ItemDto updatedItemDto) {
        Item editedItem = itemRepo.findByItemId(updatedItemDto.getItemId()).orElseThrow();
        if (updatedItemDto.getName() != null) {
            editedItem.setName(updatedItemDto.getName());
        }
        if (updatedItemDto.getDescription() != null) {
            editedItem.setDescription(updatedItemDto.getDescription());
        }
        if (updatedItemDto.getCategoryId() != null) {
            editedItem.setCategory(categoryService.getById(updatedItemDto.getCategoryId()));
        }
        editedItem.setPrice(BigDecimal.valueOf(updatedItemDto.getPrice()));
        editedItem.setAmountInStock(updatedItemDto.getAmountInStock());
        return itemRepo.save(editedItem);
    }

    public String delete(String deletedItemId) {
        Item itemToDelete = itemRepo.findByItemId(deletedItemId).orElseThrow();
        itemRepo.delete(itemToDelete);
        return itemRepo.findByItemId(deletedItemId).isPresent() ?
                String.format("Item with id=%s not deleted", deletedItemId) :
                String.format("Item with id=%s succesfully deleted", deletedItemId);
    }

    public Item getById(String itemId) {
        return itemRepo.findByItemId(itemId).orElse(null);
    }

    public List<Item> getAll(int limit, int offset, String category,
                             String name, String description, Item.Sort sort) throws InvalidSortValueException {
        return byCriteriaRepo.getItems(limit, offset, category, name, description, sort);
    }
}
