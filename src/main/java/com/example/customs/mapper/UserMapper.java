package com.example.customs.mapper;

import com.example.customs.dto.UserDTO;
import com.example.customs.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO dto) {
        return User.builder()
                .name(dto.getName())
                .unp(dto.getUnp())
                .email(dto.getEmail())
                .activityType(dto.getActivityType())
                .build();
    }

    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getName(),
                user.getUnp(),
                user.getEmail(),
                user.getActivityType()
        );
    }
}
