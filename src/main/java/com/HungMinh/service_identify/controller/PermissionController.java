package com.HungMinh.service_identify.controller;


import com.HungMinh.service_identify.dto.request.APIResponse;
import com.HungMinh.service_identify.dto.request.PermissionResquest;
import com.HungMinh.service_identify.dto.response.PermissionResponse;
import com.HungMinh.service_identify.service.PermissionService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Permission;
import java.util.List;

@Slf4j
@Builder
@RestController
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RequestMapping("/permissions")
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    APIResponse<PermissionResponse> create(@RequestBody PermissionResquest resquest){
        return APIResponse.<PermissionResponse>builder()
                .code(1001)
                .result(permissionService.create(resquest))
                .build();
    }

    @GetMapping
    APIResponse<List<PermissionResponse>> getAll(){
        List<PermissionResponse> permissionResponseList = permissionService.getAll();

        return APIResponse.<List<PermissionResponse>>builder()
                .result(permissionResponseList)
                .build();
    }

    @DeleteMapping("/{permission}")
    APIResponse<Void> delete(@PathVariable  String permission){
        permissionService.delete(permission);
        return APIResponse.<Void>builder().build();

    }


}
