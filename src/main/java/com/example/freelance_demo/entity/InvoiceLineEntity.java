package com.example.freelance_demo.entity;

import com.example.freelance_demo.entity.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
@Table(name="invoice_lines")
@EqualsAndHashCode(callSuper = true)
public class InvoiceLineEntity  extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;

    @Column(length = 500)
    private String description;

    private BigDecimal quantity = BigDecimal.ONE;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "task_id")   // optional
    private Long taskId;

    @PrePersist
    @PreUpdate
    private void calcAmount() {
        if (quantity != null && unitPrice != null) {
            amount = quantity.multiply(unitPrice)
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }
}
