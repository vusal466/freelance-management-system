package com.example.freelance_demo.service;

import com.example.freelance_demo.enums.CategoryType;
import com.example.freelance_demo.model.request.CategoryRequest;
import com.example.freelance_demo.model.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();

    List<CategoryResponse> getAllActive();

    List<CategoryResponse> searchByName(String name);

    boolean existsByName(String name);






}
