package com.HungMinh.service_identify.service;


import com.HungMinh.service_identify.dto.request.RoleResquest;
import com.HungMinh.service_identify.dto.response.RoleResponse;
import com.HungMinh.service_identify.mapper.RoleMapper;
import com.HungMinh.service_identify.repository.PermissionRepository;
import com.HungMinh.service_identify.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
// Chứa business logic (xử lý chính của ứng dụng).
@Service
@RequiredArgsConstructor // Tự động sinh constructor với tất cả các field final
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;
    public RoleResponse create (RoleResquest resquest){
        var role = roleMapper.toRole(resquest);

        var permissions = permissionRepository.findAllById(resquest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }
    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
