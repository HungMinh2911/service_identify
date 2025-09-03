package com.HungMinh.service_identify.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
// Đây là nơi chứa các object để trao đổi dữ liệu giữa client ↔ server.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults (level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 8,message = "VALID_CHECK")
    String username;

    @Size(min = 8,message = "VALID_CHECK")
    String password;
    String firstName;
     String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
     LocalDate dob;

}
