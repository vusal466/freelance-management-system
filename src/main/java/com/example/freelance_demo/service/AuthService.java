package com.example.freelance_demo.service;
import com.example.freelance_demo.model.request.LoginRequest;
import com.example.freelance_demo.model.request.RegisterRequest;
import com.example.freelance_demo.model.response.AuthResponse;
import com.example.freelance_demo.model.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}

