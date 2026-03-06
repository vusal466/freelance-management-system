package com.example.freelance_demo.entity;

import com.example.freelance_demo.config.JpaAuditConfig;
import com.example.freelance_demo.entity.base.BaseAuditEntity;
import com.example.freelance_demo.enums.ItemType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
@Table(name = "invoice_items",
        indexes = @Index(name = "idx_item_invoice", columnList = "invoice_id"))
@EqualsAndHashCode(callSuper = true)
public class InvoiceItemsEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "invoice_id",nullable = false)
    private InvoiceEntity invoice;

    @Column(name = "description",columnDefinition = "TEXT")
    private String describe;

    @Column(nullable = false,precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_price",nullable = false,precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price",nullable = false,precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private ItemType itemType = ItemType.HOURLY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_entry_id")
    private TimeEntriesEntity timeEntry;

    @PrePersist
    @PreUpdate
    public void calculateTotalPrice(){
        if(quantity !=null && unitPrice!=null){
            this.totalPrice=unitPrice.multiply(quantity)
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }

}
