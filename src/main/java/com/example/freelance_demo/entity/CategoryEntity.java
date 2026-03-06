package com.example.freelance_demo.entity;

import com.example.freelance_demo.entity.base.BaseAuditEntity;
import com.example.freelance_demo.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "categories")
@EqualsAndHashCode(callSuper = true)
public class CategoryEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true, length = 100)
    private String categoryName;

    @Column(name = "category_type",nullable = false,length = 20)
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Column(name = "is_active",nullable = false)
    private boolean isActive;

    @Column(length = 10)
    private String color;

    @Column(columnDefinition = "TEXT")
    private String description;



    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Project> projects;
}
