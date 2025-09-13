package com.HungMinh.service_identify.controller;


import com.HungMinh.service_identify.dto.request.APIResponse;
import com.HungMinh.service_identify.dto.request.PermissionResquest;
import com.HungMinh.service_identify.dto.request.RoleResquest;
import com.HungMinh.service_identify.dto.response.PermissionResponse;
import com.HungMinh.service_identify.dto.response.RoleResponse;
import com.HungMinh.service_identify.service.PermissionService;
import com.HungMinh.service_identify.service.RoleService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Builder
@RestController
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RequestMapping("/roles")
public class RoleController {

    RoleService roleService;

    @PostMapping
    APIResponse<RoleResponse> create(@RequestBody RoleResquest roleResquest){
        return APIResponse.<RoleResponse>builder()
                .code(1001)
                .result(roleService.create(roleResquest))
                .build();
    }

    @GetMapping
    APIResponse<List<RoleResponse>> getAll(){
        List<RoleResponse> roleResponseList = roleService.getAll();

        return APIResponse.<List<RoleResponse>>builder()
                .result(roleResponseList)
                .build();
    }

    @DeleteMapping("/{permission}")
    APIResponse<Void> delete(@PathVariable  String role){
        roleService.delete(role);
        return APIResponse.<Void>builder().build();

    }


}
