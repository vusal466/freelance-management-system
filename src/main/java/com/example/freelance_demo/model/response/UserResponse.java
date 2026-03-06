package com.example.freelance_demo.model.response;

import com.example.freelance_demo.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private User.Role role;
    private boolean isActive;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
