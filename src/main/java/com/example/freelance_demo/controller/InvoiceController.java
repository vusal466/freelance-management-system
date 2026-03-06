package com.example.freelance_demo.controller;

import com.example.freelance_demo.model.request.InvoiceRequest;
import com.example.freelance_demo.model.response.InvoiceResponse;
import com.example.freelance_demo.enums.InvoiceStatus;
import com.example.freelance_demo.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoice", description = "Invoice management with line items")
@SecurityRequirement(name = "BearerAuth")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Operation(summary = "Create invoice", description = "Creates invoice with line items")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Invoice created"),
            @ApiResponse(responseCode = "400", description = "Invalid data or line items"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<InvoiceResponse> create(@Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.create(request));
    }

    @Operation(summary = "Get all invoices")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Page<InvoiceResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "0")
            @Parameter(name = "page", description = "Page number", example = "0") int page,

            @RequestParam(name = "size", defaultValue = "10")
            @Parameter(name = "size", description = "Page size", example = "10") int size,

            @RequestParam(name = "sort", defaultValue = "createdAt,desc")
            @Parameter(name = "sort", description = "Sort field,direction", example = "createdAt,desc") String sort) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        return ResponseEntity.ok(invoiceService.list(pageable));
    }

    @Operation(summary = "Get invoice by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice found"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public InvoiceResponse getById(@Parameter(description = "Invoice ID") @PathVariable Long id) {
        return invoiceService.getById(id);
    }

    @Operation(summary = "Get invoices by client")
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public Page<InvoiceResponse> getByClient(@Parameter(description = "Client ID") @PathVariable Long clientId,
                                             Pageable pageable) {
        return invoiceService.findByClientId(clientId, pageable);
    }

    @Operation(summary = "Get invoices by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public Page<InvoiceResponse> getByStatus(@Parameter(description = "Status: DRAFT, SENT, PAID, OVERDUE") @PathVariable InvoiceStatus status,
                                             Pageable pageable) {
        return invoiceService.findByStatus(status, pageable);
    }

    @Operation(summary = "Get overdue invoices")
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public Page<InvoiceResponse> getOverdue(@RequestParam LocalDate date,
                                            Pageable pageable) {
        return invoiceService.findOverdue(date, pageable);
    }

    @Operation(summary = "Update invoice", description = "Updates invoice and line items")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice updated"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "409", description = "Cannot modify PAID invoice")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public InvoiceResponse update(@Parameter(description = "Invoice ID") @PathVariable Long id,
                                  @Valid @RequestBody InvoiceRequest request) {
        return invoiceService.update(id, request);
    }

    @Operation(summary = "Update invoice status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public InvoiceResponse updateStatus(@Parameter(description = "Invoice ID") @PathVariable Long id,
                                        @RequestParam InvoiceStatus status) {
        return invoiceService.updateStatus(id, status);
    }

    @Operation(summary = "Delete invoice", description = "ADMIN only, cannot delete PAID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Invoice deleted"),
            @ApiResponse(responseCode = "400", description = "Cannot delete paid invoice"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Invoice ID") @PathVariable Long id) {
        invoiceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get total revenue", description = "Sum of all paid invoices")
    @GetMapping("/revenue/total")
    @PreAuthorize("hasRole('ADMIN')")
    public BigDecimal getTotalRevenue(@RequestParam(required = false) LocalDate start,
                                      @RequestParam(required = false) LocalDate end) {
        return invoiceService.calculateRevenue(start, end);
    }
}
