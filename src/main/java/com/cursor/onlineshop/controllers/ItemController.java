package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CreateItemDto;
import com.cursor.onlineshop.dtos.ItemDto;
import com.cursor.onlineshop.entities.goods.Item;
import com.cursor.onlineshop.exceptions.InvalidSortValueException;
import com.cursor.onlineshop.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("items")
@Secured("ROLE_ADMIN")
public class ItemController {

    private final ItemService itemService;

    /**
     * GET - get a list of items.
     *
     * @param limitString  a number of returned entities
     * @param offsetString offset
     * @param category     searching condition determines whether an item category contains a string pattern
     * @param name         searching condition determines whether a name equal to string pattern
     * @param description  searching condition determines whether a description contains a string pattern
     * @param sortString   sorting condition
     * @return ResponseEntity with a list of items and HttpStatus
     */
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems(
            @RequestParam(value = "limit", defaultValue = "3", required = false) String limitString,
            @RequestParam(value = "offset", defaultValue = "0", required = false) String offsetString,
            @RequestParam(value = "category", defaultValue = "", required = false) String category,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "description", defaultValue = "", required = false) String description,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortString
    ) {
        int limit = Integer.parseInt(limitString);
        int offset = Integer.parseInt(offsetString);
        try {
            Item.Sort sort = Item.Sort.determineSortType(sortString);
            return ResponseEntity.ok(itemService.getAll(limit, offset, category, name, description, sort));
        } catch (InvalidSortValueException isve) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET - get an item by id.
     *
     * @param itemId id of returned entity
     * @return ResponseEntity with an item and HttpStatus
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable String itemId) {
        Item item = itemService.getById(itemId);
        return item != null ? ResponseEntity.ok(item) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * POST -  add item.
     *
     * @param newItemDto Item's Data Transfer Object
     * @return ResponseEntity with a created entity with id and HttpStatus
     */
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody CreateItemDto newItemDto) {
        Item newItemFromDb = itemService.add(newItemDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{itemId}").
                buildAndExpand(newItemFromDb.getItemId()).toUri();
        return ResponseEntity.created(location).body(newItemFromDb);
    }

    /**
     * PUT -  update item.
     *
     * @param itemId  id of updated item
     * @param itemDto Item's Data Transfer Object
     * @return ResponseEntity with an updated entity and HttpStatus
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<Item> editItem(@PathVariable String itemId, @RequestBody ItemDto itemDto) {
        itemDto.setItemId(itemId);
        return ResponseEntity.ok(itemService.update(itemDto));
    }

    /**
     * DELETE -  delete item.
     *
     * @param itemId id of deleted item
     * @return ResponseEntity with a message about result of entity's deleting and HttpStatus
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable String itemId) {
        return ResponseEntity.ok(itemService.delete(itemId));
    }
}
