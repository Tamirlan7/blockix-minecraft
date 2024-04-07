package com.blockix.backend.config;

import com.blockix.backend.model.Role;
import com.blockix.backend.model.User;
import com.blockix.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppInit implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(
                    User.builder()
                            .username("admin")
                            .email("admin@gmail.com")
                            .password(passwordEncoder.encode("admin12345"))
                            .role(Role.ROLE_ADMIN)
                            .build()
            );
        }
    }
}
