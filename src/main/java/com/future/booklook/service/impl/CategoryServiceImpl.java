package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Category;
import com.future.booklook.repository.CategoryRepository;
import com.future.booklook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public Category findByCategoryName(String category){
        return categoryRepository.findByCategoryName(category);
    }

    public Set<Category> findAllCategories(){
        return categoryRepository.findAllCategories();
    }

    public Boolean categoryExistByCategoryName(String category){
        return categoryRepository.existsByCategoryName(category);
    }
}
