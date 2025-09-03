package com.HungMinh.service_identify.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // chỉ đưa những field khác null vào JSON.
public class APIResponse <T> {
     int code = 1000 ;
     String message;
     T result;




}
