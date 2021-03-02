package com.cursor.onlineshop.services;

import com.cursor.onlineshop.dtos.CreateItemDto;
import com.cursor.onlineshop.dtos.ItemDto;
import com.cursor.onlineshop.entities.goods.Item;
import com.cursor.onlineshop.repositories.ItemRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemService {

    private final ItemRepo itemRepo;

    public Item add(CreateItemDto newItemDto) {
        return itemRepo.save(newItemDto.toEntity());
    }

    public Item update(ItemDto updatedItemDto) {
        return itemRepo.save(updatedItemDto.toEntity());
    }

    public String delete(String deletedItemId) {
        Item itemToDelete = itemRepo.getOne(deletedItemId);
        itemRepo.delete(itemToDelete);
        return itemRepo.getOne(deletedItemId) == null ?
                String.format("Item with id=%s succesfully deleted", deletedItemId) :
                String.format("Item with id=%s not deleted", deletedItemId);
    }

    public Item getById(String itemId) {
        return itemRepo.getOne(itemId);
    }

    public List<Item> getAll() {
        return itemRepo.findAll();
    }
}
