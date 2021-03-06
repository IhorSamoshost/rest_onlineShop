package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CreateItemDto;
import com.cursor.onlineshop.dtos.ItemDto;
import com.cursor.onlineshop.entities.goods.Item;
import com.cursor.onlineshop.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("items")
@Secured("ROLE_ADMIN")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.getAll(), HttpStatus.FOUND);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable String itemId) {
        return new ResponseEntity<>(itemService.getById(itemId), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody CreateItemDto newItemDto) {
        return new ResponseEntity<>(itemService.add(newItemDto), HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Item> editItem(@PathVariable String itemId, @RequestBody ItemDto itemDto) {
        ItemDto editedItemDto = itemService.getById(itemId).toDto();
        editedItemDto.setItemId(itemId);
        if (itemDto.getName() != null) {
            editedItemDto.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            editedItemDto.setDescription(itemDto.getDescription());
        }
        if (itemDto.getCategoryId() != null) {
            editedItemDto.setCategoryId(itemDto.getCategoryId());
        }
        editedItemDto.setPrice(itemDto.getPrice());
        editedItemDto.setAmountInStock(itemDto.getAmountInStock());
        return new ResponseEntity<>(itemService.update(editedItemDto), HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable String itemId) {
        return new ResponseEntity<>(itemService.delete(itemId), HttpStatus.OK);
    }
}