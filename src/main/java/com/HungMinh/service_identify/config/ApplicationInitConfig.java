package com.HungMinh.service_identify.config;

import com.HungMinh.service_identify.entity.User;
import com.HungMinh.service_identify.enums.Role;
import com.HungMinh.service_identify.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE,makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    @Bean
    // nhúng code chạy ngay sau khi Spring Boot start và trước khi app nhận request đầu tiên.
    ApplicationRunner applicationRunner (UserRepository userRepository){
        return  args -> {
            if (userRepository.findByUsername("admin").isEmpty()){
                var roles = new HashSet<String>();

                roles.add(Role.ADMIN.name());

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin has been created");
            }
        };
    }
}
