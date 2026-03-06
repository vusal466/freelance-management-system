package com.example.freelance_demo.service;

import com.example.freelance_demo.entity.User;
import com.example.freelance_demo.model.request.UserRequest;
import com.example.freelance_demo.model.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserResponse create(UserRequest request);
    UserResponse update(Long id, UserRequest request);
    void delete(Long id);
    UserResponse getById(Long id);
    Page<UserResponse> list (Pageable pageable);
    UserDetails loadUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String Email);
    UserResponse toggleActive(Long id);
    UserResponse changeRole(Long id, User.Role role);
}
