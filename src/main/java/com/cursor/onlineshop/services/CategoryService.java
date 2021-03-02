package com.cursor.onlineshop.services;

import com.cursor.onlineshop.dtos.CategoryDto;
import com.cursor.onlineshop.dtos.CreateCategoryDto;
import com.cursor.onlineshop.entities.goods.Category;
import com.cursor.onlineshop.repositories.CategoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public Category add(CreateCategoryDto newCategoryDto) {
        return categoryRepo.save(newCategoryDto.toEntity());
    }

    public Category update(CategoryDto updatedCategoryDto) {
        return categoryRepo.save(updatedCategoryDto.toEntity());
    }

    public String delete(String deletedCategoryId) {
        Category categoryToDelete = categoryRepo.getOne(deletedCategoryId);
        categoryRepo.delete(categoryToDelete);
        return categoryRepo.getOne(deletedCategoryId) == null ?
                String.format("Category with id=%s succesfully deleted", deletedCategoryId) :
                String.format("Category with id=%s not deleted", deletedCategoryId);
    }

    public Category getById(String categoryId) {
        return categoryRepo.getOne(categoryId);
    }

    public List<Category> getAll() {
        return categoryRepo.findAll();
    }
}
