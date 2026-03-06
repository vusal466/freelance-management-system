package com.example.freelance_demo.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClientResponse {
    private Long id;
    private String fullName;
    private String companyName;
    private String email;
    private String phone;
    private String address;
    private String notes;
    private boolean active;
    private LocalDateTime createdAt;
}
