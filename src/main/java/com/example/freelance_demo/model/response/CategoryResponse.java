package com.example.freelance_demo.model.response;

import com.example.freelance_demo.enums.CategoryType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CategoryResponse {

    private Long id;
    private String categoryName;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;


}
