package com.example.freelance_demo.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceLineRequest {
        @Size(max = 500)
        private String description;

        @NotNull
        @Min(1)
        private Integer quantity;

        @NotNull
        @DecimalMin("0.01")
        private BigDecimal unitPrice;

}
