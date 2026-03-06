package com.example.freelance_demo.model.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InvoiceLineResponse {
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
}