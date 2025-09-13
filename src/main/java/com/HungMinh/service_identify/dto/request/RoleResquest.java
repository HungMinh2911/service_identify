package com.HungMinh.service_identify.dto.request;


import com.HungMinh.service_identify.dto.response.PermissionResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResquest {
    String name;
    String description;
    Set<String> permissions;

}
