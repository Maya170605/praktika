package com.example.customs.mapper;

import com.example.customs.dto.UserDTO;
import com.example.customs.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO dto);
    UserDTO toDto(User entity);
}
