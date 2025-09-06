package com.HungMinh.service_identify.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MapperRespone {
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

}
