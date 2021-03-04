package com.cursor.onlineshop.services;

import com.cursor.onlineshop.dtos.CreateItemDto;
import com.cursor.onlineshop.dtos.ItemDto;
import com.cursor.onlineshop.entities.goods.Item;
import com.cursor.onlineshop.repositories.ItemRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class ItemService {

    private final ItemRepo itemRepo;
    private final CategoryService categoryService;

    public Item add(CreateItemDto newItemDto) {
        Item newItem = new Item(newItemDto.getName(), newItemDto.getDescription(),
                BigDecimal.valueOf(newItemDto.getPrice()), newItemDto.getAmountInStock(),
                categoryService.getById(newItemDto.getCategoryId()));
        return itemRepo.save(newItem);
    }

    public Item update(ItemDto updatedItemDto) {
        Item updatedItem = new Item(updatedItemDto.getItemId(), updatedItemDto.getName(),
                updatedItemDto.getDescription(), BigDecimal.valueOf(updatedItemDto.getPrice()),
                updatedItemDto.getAmountInStock(), categoryService.getById(updatedItemDto.getCategoryId()));
        return itemRepo.save(updatedItem);
    }

    public String delete(String deletedItemId) {
        Item itemToDelete = itemRepo.findByItemId(deletedItemId).orElseThrow();
        itemRepo.delete(itemToDelete);
        return itemRepo.findByItemId(deletedItemId).isPresent() ?
                String.format("Item with id=%s not deleted", deletedItemId) :
                String.format("Item with id=%s succesfully deleted", deletedItemId);
    }

    public Item getById(String itemId) {
        return itemRepo.findByItemId(itemId).orElseThrow();
    }

    public List<Item> getAll() {
        return itemRepo.findAll();
    }
}
