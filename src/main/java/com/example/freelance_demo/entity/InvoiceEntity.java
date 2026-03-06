package com.example.freelance_demo.entity;

import com.example.freelance_demo.config.JpaAuditConfig;
import com.example.freelance_demo.entity.base.BaseAuditEntity;
import com.example.freelance_demo.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="invoices")
@ToString(exclude = {"client","project"})
@EqualsAndHashCode(callSuper = true, exclude = {"client","project"})
public class InvoiceEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number",nullable = false,unique = true)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<InvoiceLineEntity> lines = new ArrayList<>();

    private LocalDate invoiceDate;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Column(nullable = false)
    private BigDecimal subTotal;

    @Column(name = "discount_amount",nullable = false,precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "total_amount", nullable = false,precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 100)
    private String notes;
    @Column(name = "payment_terms", length = 100)
    private String paymentTerms;

    private LocalDateTime paidAt;

    @PrePersist
    @PreUpdate
    private void onSave(){
        if (status==InvoiceStatus.PAID){
            if (paidAt==null){
                paidAt=LocalDateTime.now();
            }
        } else{
            paidAt=null;
        }

        if (subTotal==null) subTotal=BigDecimal.ZERO;
        if (discountAmount==null) discountAmount=BigDecimal.ZERO;

        totalAmount=subTotal.subtract(discountAmount);
    }

    public void addLine(InvoiceLineEntity line) {
        lines.add(line);
        line.setInvoice(this);
    }

    public void clearLines() {
        lines.clear();
    }

}
