package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.User;
import com.example.freelance_demo.model.request.UserRequest;
import com.example.freelance_demo.model.response.UserResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-05T21:51:23+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( request.getUsername() );
        user.setEmail( request.getEmail() );
        user.setFullName( request.getFullName() );
        user.setRole( request.getRole() );

        return user;
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( user.getId() );
        userResponse.setUsername( user.getUsername() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setFullName( user.getFullName() );
        userResponse.setRole( user.getRole() );
        userResponse.setActive( user.isActive() );
        userResponse.setCreatedAt( user.getCreatedAt() );
        userResponse.setCreatedBy( user.getCreatedBy() );
        userResponse.setUpdatedAt( user.getUpdatedAt() );
        userResponse.setUpdatedBy( user.getUpdatedBy() );

        return userResponse;
    }
}
