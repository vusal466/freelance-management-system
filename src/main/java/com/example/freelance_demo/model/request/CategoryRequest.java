package com.example.freelance_demo.model.request;

import com.example.freelance_demo.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Category name required")
    private String categoryName;

    private String description;

    private CategoryType categoryType;

}
