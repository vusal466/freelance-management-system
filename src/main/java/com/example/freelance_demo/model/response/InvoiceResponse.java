package com.example.freelance_demo.model.response;

import com.example.freelance_demo.enums.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;

    private Long clientId;
    private String clientName;

    private Long projectId;
    private String projectTitle;

    private LocalDate invoiceDate;
    private LocalDate dueDate;

    private BigDecimal subTotal;
    private BigDecimal totalAmount;

    private InvoiceStatus status;
    private String notes;

    private List<InvoiceLineResponse> lines;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




}
