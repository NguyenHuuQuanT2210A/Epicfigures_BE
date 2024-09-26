package com.example.userservice.mappers;

import com.example.userservice.dtos.request.UserRequest;
import com.example.userservice.dtos.response.UserResponse;
import com.example.userservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse toUserResponse(User user);

//    @Mapping(target = "roles", ignore = true)
//    User toUser(UserRequest request);
//    User userResponsetoUser(UserResponse response);
}