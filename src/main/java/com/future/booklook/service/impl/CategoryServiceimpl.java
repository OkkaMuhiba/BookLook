package com.future.booklook.service.impl;

import com.future.booklook.repository.CategoryRepository;
import com.future.booklook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceimpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
}
