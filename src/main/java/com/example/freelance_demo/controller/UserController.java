package com.example.freelance_demo.controller;

import com.example.freelance_demo.model.request.UserRequest;
import com.example.freelance_demo.model.response.UserResponse;
import com.example.freelance_demo.entity.User;
import com.example.freelance_demo.service.UserService;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Admin user management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create user", description = "ADMIN only - creates new system user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Username or email exists")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @Operation(summary = "Get all users", description = "ADMIN only - paginated user list")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getAll( @Parameter(description = "Page number", example = "0")
                                          @RequestParam(defaultValue = "0") int page,

                                      @Parameter(description = "Page size", example = "20")
                                          @RequestParam(defaultValue = "20") int size,

                                      @Parameter(description = "Sort field", example = "id")
                                          @RequestParam(defaultValue = "id") String sortBy,

                                      @Parameter(description = "Sort direction", example = "ASC")
                                          @RequestParam(defaultValue = "ASC") String direction)  {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userService.list(pageable);
    }

    @Operation(summary = "Get user by ID", description = "ADMIN only")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getById(@Parameter(description = "User ID") @PathVariable Long id) {
        return userService.getById(id);
    }

    @Operation(summary = "Check username exists")
    @GetMapping("/check-username")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean checkUsername(@RequestParam String username) {
        return userService.existsByUsername(username);
    }

    @Operation(summary = "Check email exists")
    @GetMapping("/check-email")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean checkEmail(@RequestParam String email) {
        return userService.existsByEmail(email);
    }

    @Operation(summary = "Update user", description = "ADMIN only")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Username/email taken")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse update(@Parameter(description = "User ID") @PathVariable Long id,
                               @Valid @RequestBody UserRequest request) {
        return userService.update(id, request);
    }

    @Operation(summary = "Toggle user active status", description = "Enable/disable user account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status toggled"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/toggle-active")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse toggleActive(@Parameter(description = "User ID") @PathVariable Long id) {
        return userService.toggleActive(id);
    }

    @Operation(summary = "Change user role", description = "ADMIN only")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role changed"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse changeRole(@Parameter(description = "User ID") @PathVariable Long id,
                                   @RequestParam User.Role role) {
        return userService.changeRole(id, role);
    }

    @Operation(summary = "Delete user", description = "ADMIN only - permanent delete")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "User ID") @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}