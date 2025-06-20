package com.example.customs.mapper;

import com.example.customs.dto.UserDTO;
import com.example.customs.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UnpMapperHelper.class)
public interface UserMapper {

    @Mapping(source = "unp", target = "unp", qualifiedByName = "mapUnpFromString")
    User toEntity(UserDTO dto);

    @Mapping(source = "unp.unp", target = "unp")
    UserDTO toDto(User entity);
}
