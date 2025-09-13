package com.HungMinh.service_identify.mapper;

import com.HungMinh.service_identify.dto.request.PermissionResquest;
import com.HungMinh.service_identify.dto.request.RoleResquest;
import com.HungMinh.service_identify.dto.response.PermissionResponse;
import com.HungMinh.service_identify.dto.response.RoleResponse;
import com.HungMinh.service_identify.entity.Permision;
import com.HungMinh.service_identify.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions",ignore = true)
    Role toRole (RoleResquest resquest);
    RoleResponse toRoleResponse(Role role);
}
