package com.example.freelance_demo.model.request;

import com.example.freelance_demo.enums.InvoiceStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceRequest {
    @NotNull(message = "Client is mandatory")
    private Long clientId;

    private Long projectId;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or later")
    private LocalDate dueDate;

    @NotNull
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @Size(max = 100, message = "Payment terms too long")
    private String paymentTerms;


    @Valid
    @NotEmpty(message = "At least one line item is required")
    private List<InvoiceLineRequest> lines;

}
