package com.HungMinh.service_identify.service;

import com.HungMinh.service_identify.dto.request.PermissionResquest;
import com.HungMinh.service_identify.dto.response.PermissionResponse;
import com.HungMinh.service_identify.entity.Permision;
import com.HungMinh.service_identify.mapper.PermissionMapper;
import com.HungMinh.service_identify.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
// Chứa business logic (xử lý chính của ứng dụng).
@Service
@RequiredArgsConstructor // Tự động sinh constructor với tất cả các field final
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    public PermissionResponse create(PermissionResquest request){
        Permision permision = permissionMapper.toPermission(request);
        permision = permissionRepository.save(permision);

        return  permissionMapper.toPermissionResponse(permision);
    }

    public List<PermissionResponse> getAll(){
        var permissions= permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
