package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Category;
import com.future.booklook.repository.CategoryRepository;
import com.future.booklook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public Category findCategoryByCategoryName(String category){
        return categoryRepository.findCategoryByCategoryName(category);
    }
}
