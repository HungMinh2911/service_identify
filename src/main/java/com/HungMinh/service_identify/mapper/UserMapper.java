package com.HungMinh.service_identify.mapper;

import com.HungMinh.service_identify.dto.request.UserCreationRequest;
import com.HungMinh.service_identify.dto.request.UserUpdateRequest;
import com.HungMinh.service_identify.dto.response.MapperRespone;
import org.mapstruct.Mapper;
import com.HungMinh.service_identify.entity.User;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "password", target = "password")
    MapperRespone toMapperRespone (User user);
    List<MapperRespone> toListMapper (List<User> user);
}
