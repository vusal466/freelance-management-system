package com.example.freelance_demo.service.impl;

import com.example.freelance_demo.entity.User;
import com.example.freelance_demo.exception.AlreadyExistsException;
import com.example.freelance_demo.exception.ResourceNotFoundException;
import com.example.freelance_demo.mapper.UserMapper;
import com.example.freelance_demo.model.request.UserRequest;
import com.example.freelance_demo.model.response.UserResponse;
import com.example.freelance_demo.repositories.UserRepository;
import com.example.freelance_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final @Lazy PasswordEncoder passEncoder;

    @Override
    @Transactional
    public UserResponse create(UserRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        // Yoxla
        if(existsByUsername(username)) {
            throw new AlreadyExistsException("Username already taken");
        }
        if(existsByEmail(email)) {
            throw new AlreadyExistsException("Email already registered");
        }


        User user = userMapper.toEntity(request);
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passEncoder.encode(request.getPassword()));
        user.setActive(true);

        User saved = userRepository.save(user);

        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        if(!user.getUsername().equals(request.getUsername()) && existsByUsername(request.getUsername())) throw new IllegalArgumentException("username already taken");
        if (!user.getEmail().equals(request.getEmail())&& existsByEmail(request.getEmail())) throw new IllegalArgumentException("email alredy taken");

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if(!userRepository.existsById(id)) throw new ResourceNotFoundException("User not found");
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public Page<UserResponse> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));    }

    @Override
    @Transactional
    public UserResponse toggleActive(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setActive(!user.isActive());  // Toggle: true ↔ false

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse changeRole(Long id, User.Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRole(role);

        return userMapper.toResponse(userRepository.save(user));
    }

}
