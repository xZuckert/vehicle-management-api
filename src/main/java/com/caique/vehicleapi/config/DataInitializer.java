package com.caique.vehicleapi.config;

import com.caique.vehicleapi.dto.LoginRequest;
import com.caique.vehicleapi.model.AppUser;
import com.caique.vehicleapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner init() {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                AppUser user = new AppUser();
                user.setUsername("admin");
                user.setPassword(encoder.encode("123"));
                user.setRoles(List.of("ROLE_ADMIN"));

                repo.save(user);
            }
        };
    }
}
