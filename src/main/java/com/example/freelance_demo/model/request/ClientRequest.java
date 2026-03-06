package com.example.freelance_demo.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientRequest {

    @NotBlank(message = "full name is required")
    private String fullName;
    private String companyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private String address;

    private String notes;

}
