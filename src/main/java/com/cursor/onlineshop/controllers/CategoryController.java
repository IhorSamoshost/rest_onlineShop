package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CategoryDto;
import com.cursor.onlineshop.dtos.CreateCategoryDto;
import com.cursor.onlineshop.entities.goods.Category;
import com.cursor.onlineshop.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
@Secured("ROLE_ADMIN")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * GET - get a list of categories sorted by names.
     *
     * @param limitString  a number of returned entities
     * @param offsetString offset
     * @param name         searching condition determines whether a name equal to string pattern
     * @param description  searching condition determines whether a description contains a string pattern
     * @return ResponseEntity with a list of categories and HttpStatus
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam(value = "limit", defaultValue = "3", required = false) String limitString,
            @RequestParam(value = "offset", defaultValue = "0", required = false) String offsetString,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "description", defaultValue = "", required = false) String description
    ) {
        int limit = Integer.parseInt(limitString);
        int offset = Integer.parseInt(offsetString);
        return ResponseEntity.ok(categoryService.getAll(limit, offset, name, description));
    }

    /**
     * GET - get a category by id.
     *
     * @param categoryId id of returned entity
     * @return ResponseEntity with a category and HttpStatus
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String categoryId) {
        return ResponseEntity.ok(categoryService.getById(categoryId));
    }

    /**
     * POST -  add category
     *
     * @param newCategoryDto Category's Data Transfer Object
     * @return ResponseEntity with a created entity with id and HttpStatus
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryDto newCategoryDto) {
        Category newCategoryFromDb = categoryService.add(newCategoryDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{categoryId}").
                buildAndExpand(newCategoryFromDb.getCategoryId()).toUri();
        return ResponseEntity.created(location).body(newCategoryFromDb);
    }

    /**
     * PUT -  update category
     *
     * @param categoryId  id of updated category
     * @param categoryDto Category's Data Transfer Object
     * @return ResponseEntity with an updated entity and HttpStatus
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> editCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        categoryDto.setCategoryId(categoryId);
        return ResponseEntity.ok(categoryService.update(categoryDto));
    }

    /**
     * DELETE -  delete category
     *
     * @param categoryId id of deleted category
     * @return ResponseEntity with a message about result of entity's deleting and HttpStatus
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(categoryService.delete(categoryId));
    }
}
