package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.User;
import com.example.freelance_demo.model.request.UserRequest;
import com.example.freelance_demo.model.response.UserResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);
}