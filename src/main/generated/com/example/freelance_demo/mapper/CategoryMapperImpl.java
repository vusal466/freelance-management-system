package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.CategoryEntity;
import com.example.freelance_demo.model.request.CategoryRequest;
import com.example.freelance_demo.model.response.CategoryResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-05T21:51:24+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponse toResponse(CategoryEntity category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setId( category.getId() );
        categoryResponse.setCategoryName( category.getCategoryName() );
        categoryResponse.setDescription( category.getDescription() );
        categoryResponse.setActive( category.isActive() );
        categoryResponse.setCreatedAt( category.getCreatedAt() );

        return categoryResponse;
    }

    @Override
    public CategoryEntity toEntity(CategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setCategoryName( request.getCategoryName() );
        categoryEntity.setCategoryType( request.getCategoryType() );
        categoryEntity.setDescription( request.getDescription() );

        return categoryEntity;
    }

    @Override
    public void updateEntityFromRequest(CategoryRequest request, CategoryEntity category) {
        if ( request == null ) {
            return;
        }

        if ( request.getCategoryName() != null ) {
            category.setCategoryName( request.getCategoryName() );
        }
        if ( request.getCategoryType() != null ) {
            category.setCategoryType( request.getCategoryType() );
        }
        if ( request.getDescription() != null ) {
            category.setDescription( request.getDescription() );
        }
    }
}
