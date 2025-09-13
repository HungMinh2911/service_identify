package com.HungMinh.service_identify.config;

import com.HungMinh.service_identify.dto.request.APIResponse;
import com.HungMinh.service_identify.excepytion.ErorrCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntrypoint implements AuthenticationEntryPoint {
    @Override
//    HttpServletRequest request → chứa thông tin request gốc từ client.
//    HttpServletResponse response → nơi bạn ghi response trả về cho client.
//    AuthenticationException authException → Exception Spring Security ném ra
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErorrCode erorrCode = ErorrCode.UNAUTHENTICATED;

        response.setStatus(erorrCode.getHttpStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //tức là server sẽ trả về JSON.

        APIResponse<?> apiResponse = APIResponse.builder()
                .code(erorrCode.getCode())
                .message(erorrCode.getMessage())
                .build();
        ObjectMapper objectMapper = new ObjectMapper(); //object apiResponse thành JSON string.

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse)); // ghi JSON đó vào body response để client nhận được.
        response.flushBuffer(); // Đẩy dữ liệu xuống socket ngay lập tức, đảm bảo client nhận response
    }
}
// Tại sao không trả về 1 APIResponse bình thường?
//→ Vì request chưa vào Controller, không qua @ControllerAdvice. Do đó, Spring Security chỉ cho bạn một cái HttpServletResponse, bạn phải tự viết JSON vào đó.
//Nhưng vẫn trả về APIResponse mà 🤔
//→ Đúng, bạn vẫn đang trả APIResponse, chỉ khác là không return object như ở Controller, mà convert object → JSON rồi viết thẳng vào HttpServletResponse.
//Đây là cách Spring Security cho phép bạn giữ format JSON đồng nhất với các API còn lại.
