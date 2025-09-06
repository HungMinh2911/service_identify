package com.HungMinh.service_identify.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final  String[] PUBLIC_ENDPOINT = {"/users","/auth/token","/auth/introspaect"};
    @Value("${jwt.signerKey}")
    private  String signerKey ;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {


        // httpSecurity.authorizeHttpRequests(...)  Bật cấu hình phân quyền cho các request.
        httpSecurity.authorizeHttpRequests(request ->
                // Cho phép tất cả mọi người (không cần đăng nhập, không cần token) gọi API POST /users.
                request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINT).permitAll()
                        .anyRequest().authenticated() // Tất cả các request khác (ngoại trừ POST /users) đều yêu cầu người dùng đã đăng nhập và có JWT token hợp lệ.
                );

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                );
        //  csrf  là cơ chế bảo vệ chống tấn công CSRF
        // gọi hàm disable() để tắt CSRF protection.

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder (){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(),"HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

}