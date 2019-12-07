package com.future.booklook.controller;


import com.future.booklook.model.entity.Category;
import com.future.booklook.payload.ApiResponse;
import com.future.booklook.payload.CreateCategory;
import com.future.booklook.service.impl.CategoryServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    CategoryServiceImpl categoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CreateCategory category){
        categoryService.save(new Category(category.getCategoryName()));
        return new ResponseEntity(new ApiResponse(true, "Category created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> showAllCategories(){
        return new ResponseEntity(categoryService.findAllCategories(), HttpStatus.OK);
    }
}
