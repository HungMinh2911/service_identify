package com.HungMinh.service_identify.config;

import com.HungMinh.service_identify.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final  String[] PUBLIC_ENDPOINT = {"/users","/auth/token","/auth/introspaect"};
    @Value("${jwt.signerKey}")
    private  String signerKey ;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {


        // httpSecurity.authorizeHttpRequests(...)  Bật cấu hình phân quyền cho các request.
        httpSecurity.authorizeHttpRequests(
                request ->
                // Cho phép tất cả mọi người (không cần đăng nhập, không cần token) gọi API POST /users.
                        request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINT).permitAll()
                                .anyRequest().authenticated()// Tất cả các request khác (ngoại trừ POST /users) đều yêu cầu người dùng đã đăng nhập và có JWT token hợp lệ.
                );
        // .jwt(...) để nói “Token mình nhận là JWT
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntrypoint())
                //Spring Security phát hiện request chưa login hoặc token không hợp lệ, nó sẽ không cho vào Controller,
                );
        //  csrf  là cơ chế bảo vệ chống tấn công CSRF
        // gọi hàm disable() để tắt CSRF protection.

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder (){ // kiểm tra tính hợp lệ của JWT kiểm tra chữ ký
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(),"HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec) // dùng secret để verify chữ ký
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter((jwtGrantedAuthoritiesConverter));

        return jwtAuthenticationConverter;
    }

}