package com.online.order.config;

import com.online.order.entity.User;
import com.online.order.enums.Role;
import com.online.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepo.existsByEmail("admin@food.com")) return;

        User admin = new User();
        admin.setName("Admin");
        admin.setEmail("admin@food.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepo.save(admin);
    }
}