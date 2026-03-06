package com.example.freelance_demo.controller;

import com.example.freelance_demo.model.request.PaymentRequest;
import com.example.freelance_demo.model.response.PaymentResponse;
import com.example.freelance_demo.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment management APIs")
@SecurityRequirement(name = "BearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Create payment", description = "Records a new payment for an invoice")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Payment created"),
            @ApiResponse(responseCode = "400", description = "Payment exceeds invoice balance"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.create(request));
    }

    @Operation(summary = "Get payment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public PaymentResponse getById(@Parameter(description = "Payment ID") @PathVariable Long id) {
        return paymentService.getById(id);
    }

    @Operation(summary = "Get payments by invoice")
    @GetMapping("/invoice/{invoiceId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<PaymentResponse> getByInvoice(@Parameter(description = "Invoice ID") @PathVariable Long invoiceId) {
        return paymentService.findByInvoice(invoiceId);
    }

    @Operation(summary = "Get payments by invoice (paginated)")
    @GetMapping("/invoice/{invoiceId}/page")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public Page<PaymentResponse> getByInvoicePage(@Parameter(description = "Invoice ID") @PathVariable Long invoiceId,
                                                  Pageable pageable) {
        return paymentService.listByInvoice(invoiceId, pageable);
    }

    @Operation(summary = "Get total paid amount for invoice")
    @GetMapping("/invoice/{invoiceId}/total")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public BigDecimal getTotalPaid(@Parameter(description = "Invoice ID") @PathVariable Long invoiceId) {
        return paymentService.totalPaid(invoiceId);
    }

    @Operation(summary = "Get remaining balance for invoice")
    @GetMapping("/invoice/{invoiceId}/balance")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public BigDecimal getBalance(@Parameter(description = "Invoice ID") @PathVariable Long invoiceId) {
        return paymentService.calculateBalance(invoiceId);
    }

    @Operation(summary = "Update payment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment updated"),
            @ApiResponse(responseCode = "400", description = "Invalid amount"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PaymentResponse update(@Parameter(description = "Payment ID") @PathVariable Long id,
                                  @Valid @RequestBody PaymentRequest request) {
        return paymentService.update(id, request);
    }

    @Operation(summary = "Delete payment", description = "ADMIN only")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Payment deleted"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Payment ID") @PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}