package com.example.freelance_demo.repositories;

import com.example.freelance_demo.entity.CategoryEntity;
import com.example.freelance_demo.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsByCategoryName(String name);

    List<CategoryEntity> findByCategoryNameContainingIgnoreCase(String categoryName);

    @Query("SELECT c FROM CategoryEntity c WHERE c.isActive=true order by c.categoryName")
    List<CategoryEntity> findAllActive();

    @Query("select c from CategoryEntity c where c.isActive = true and c.categoryType = :type order by c.categoryName")
    List<CategoryEntity> findAllActiveByType(@Param("type") CategoryType type);

    @Query("select c from CategoryEntity c where c.isActive = true order by c.categoryName")
    Page<CategoryEntity> findAllActivePageable(Pageable pageable);
}
