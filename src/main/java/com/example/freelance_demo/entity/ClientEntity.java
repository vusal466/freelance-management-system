package com.example.freelance_demo.entity;

import com.example.freelance_demo.entity.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;



@Entity
@Data
@Table(name="clients")
@EqualsAndHashCode(callSuper = true)
public class ClientEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(length = 100)
    private String companyName;

    @Column(length = 255)
    private String address;

    @Column(unique = true,length = 80)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;


}
