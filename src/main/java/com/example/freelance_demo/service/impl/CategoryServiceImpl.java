package com.example.freelance_demo.service.impl;

import com.example.freelance_demo.entity.CategoryEntity;
import com.example.freelance_demo.exception.ResourceNotFoundException;
import com.example.freelance_demo.mapper.CategoryMapper;
import com.example.freelance_demo.model.request.CategoryRequest;
import com.example.freelance_demo.model.response.CategoryResponse;
import com.example.freelance_demo.repositories.CategoryRepository;
import com.example.freelance_demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if(categoryRepository.existsByCategoryName(request.getCategoryName())){
            throw new IllegalArgumentException("Category already exists: " + request.getCategoryName());
        }

        if(request.getCategoryType() == null) {
            throw new IllegalArgumentException("Category type is required");
        }

        CategoryEntity category = categoryMapper.toEntity(request);
        category.setActive(true);

        CategoryEntity saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(
                "Category not found with id"+id
        ));
        if(!category.getCategoryName().equals(request.getCategoryName()) && categoryRepository.existsByCategoryName(request.getCategoryName())){
            throw new IllegalArgumentException(
                    "category  name already taken: " +request.getCategoryName()
            );
        }
        categoryMapper.updateEntityFromRequest(request,category);
        CategoryEntity updated = categoryRepository.save(category);

        return categoryMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Category not found with id: "+id));
        category.setActive(false);
        categoryRepository.save(category);
    }


    @Override
    public CategoryResponse getById(Long id) {
        return categoryRepository.findById(id).map(
                categoryMapper::toResponse)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Category not found with id: "+id));
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository
                .findAll().stream().map(categoryMapper::toResponse).toList();
    }

    @Override
    public List<CategoryResponse> getAllActive() {
        return categoryRepository
                .findAllActive().stream().map(categoryMapper::toResponse).toList();
    }

    @Override
    public List<CategoryResponse> searchByName(String name) {
        return categoryRepository
                .findByCategoryNameContainingIgnoreCase(name)
                .stream().map(categoryMapper::toResponse).toList();
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByCategoryName(name);
    }
}
