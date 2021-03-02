package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CategoryDto;
import com.cursor.onlineshop.dtos.CreateCategoryDto;
import com.cursor.onlineshop.entities.goods.Category;
import com.cursor.onlineshop.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.FOUND);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String categoryId) {
        return new ResponseEntity<>(categoryService.getById(categoryId), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoryService.add(newCategoryDto), HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> editCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        categoryDto.setCategoryId(categoryId);
        return new ResponseEntity<>(categoryService.update(categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryId) {
        return new ResponseEntity<>(categoryService.delete(categoryId), HttpStatus.OK);
    }
}
