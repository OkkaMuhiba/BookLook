package com.future.booklook.payload.request;

import javax.validation.constraints.NotBlank;

public class CreateCategoryRequest {
    @NotBlank
    String CategoryName;

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
