package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.CategoryEntity;
import com.example.freelance_demo.model.request.CategoryRequest;
import com.example.freelance_demo.model.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {
    CategoryResponse toResponse(CategoryEntity category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projects", ignore = true)
    CategoryEntity toEntity(CategoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projects", ignore = true)
    void updateEntityFromRequest(CategoryRequest request, @MappingTarget CategoryEntity category);}
