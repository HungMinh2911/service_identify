package com.HungMinh.service_identify.mapper;

import com.HungMinh.service_identify.dto.request.PermissionResquest;
import com.HungMinh.service_identify.dto.request.UserCreationRequest;
import com.HungMinh.service_identify.dto.request.UserUpdateRequest;
import com.HungMinh.service_identify.dto.response.MapperRespone;
import com.HungMinh.service_identify.dto.response.PermissionResponse;
import com.HungMinh.service_identify.entity.Permision;
import com.HungMinh.service_identify.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permision toPermission (PermissionResquest resquest);
    PermissionResponse toPermissionResponse(Permision permision);
}
