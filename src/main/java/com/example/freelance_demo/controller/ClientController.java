package com.example.freelance_demo.controller;

import com.example.freelance_demo.model.request.ClientRequest;
import com.example.freelance_demo.model.response.ClientResponse;
import com.example.freelance_demo.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Client management APIs")
@SecurityRequirement(name = "BearerAuth")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Create new client", description = "Creates a new client (ADMIN or MANAGER only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "409", description = "Email or phone already exists")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(request));
    }

    @Operation(summary = "Get all clients", description = "Retrieves all clients (paginated)")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public Page<ClientResponse> getAll(@Parameter(description = "Page number", example = "0")
                                           @RequestParam(defaultValue = "0") int page,

                                       @Parameter(description = "Page size", example = "20")
                                           @RequestParam(defaultValue = "20") int size,

                                       @Parameter(description = "Sort field", example = "id")
                                           @RequestParam(defaultValue = "id") String sortBy,

                                       @Parameter(description = "Sort direction", example = "ASC")
                                           @RequestParam(defaultValue = "ASC") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return clientService.list(pageable);
    }

    @Operation(summary = "Get client by ID", description = "Retrieves a single client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ClientResponse getById(@Parameter(description = "Client ID") @PathVariable Long id) {
        return clientService.getById(id);
    }

    @Operation(summary = "Search clients by name", description = "Full-text search on client name")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<ClientResponse> search(@RequestParam(required = false) String keyword) {
        return clientService.searchByName(keyword);
    }

    @Operation(summary = "Search clients by company", description = "Search by company name")
    @GetMapping("/search/company")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<ClientResponse> searchByCompany(@RequestParam String company) {
        return clientService.searchByCompany(company);
    }

    @Operation(summary = "Global search", description = "Search in name, company, email, phone")
    @GetMapping("/search/global")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<ClientResponse> globalSearch(@RequestParam String keyword) {
        return clientService.globalSearch(keyword);
    }

    @Operation(summary = "Update client", description = "Updates an existing client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client updated"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "409", description = "Email/phone already taken")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ClientResponse update(@Parameter(description = "Client ID") @PathVariable Long id,
                                 @Valid @RequestBody ClientRequest request) {
        return clientService.update(id, request);
    }

    @Operation(summary = "Delete client", description = "Soft deletes a client (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Client ID") @PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check email exists")
    @GetMapping("/check-email")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public boolean checkEmail(@RequestParam String email) {
        return clientService.existsByEmail(email);
    }

    @Operation(summary = "Check phone exists")
    @GetMapping("/check-phone")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public boolean checkPhone(@RequestParam String phone) {
        return clientService.existsByPhone(phone);
    }

    @Operation(summary = "Count active clients")
    @GetMapping("/count/active")
    @PreAuthorize("hasRole('ADMIN')")
    public long countActive() {
        return clientService.countActive();
    }
}