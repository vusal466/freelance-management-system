package com.example.freelance_demo.service.impl;

import com.example.freelance_demo.model.request.LoginRequest;
import com.example.freelance_demo.model.request.RegisterRequest;
import com.example.freelance_demo.model.request.UserRequest;
import com.example.freelance_demo.model.response.AuthResponse;
import com.example.freelance_demo.model.response.UserResponse;
import com.example.freelance_demo.security.JwtUtils;
import com.example.freelance_demo.service.AuthService;
import com.example.freelance_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtUtils jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponse register(RegisterRequest request) {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(request.getUsername());
        userRequest.setEmail(request.getEmail());
        userRequest.setPassword(request.getPassword());
        userRequest.setFullName(request.getFullName());
        userRequest.setRole(request.getRole());

        return userService.create(userRequest);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );


        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());


        String role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("USER");


        String token = jwtUtil.generateToken(userDetails.getUsername(), role);

        return AuthResponse.builder()
                .accessToken(token)
                .username(userDetails.getUsername())
                .role(role)
                .build();
    }
}