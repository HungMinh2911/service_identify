package com.HungMinh.service_identify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

// Đây là nơi để các class ánh xạ trực tiếp với bảng trong database (JPA Entity).
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permision {
    @Id
    String name;
    String description;

}
