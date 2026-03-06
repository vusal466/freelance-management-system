package com.example.freelance_demo.entity;

import com.example.freelance_demo.entity.base.BaseAuditEntity;
import com.example.freelance_demo.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payments")
@EqualsAndHashCode(callSuper = true)
public class PaymentEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "invoice_id",nullable = false)
    private InvoiceEntity invoice;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Positive(message = "Amount must be greater than 0")
    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_method", nullable = false)
    private PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;

    @Column(name = "reference_number")
    private String referenceNumber;

    private String notes;

}
