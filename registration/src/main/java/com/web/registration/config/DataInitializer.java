package com.web.registration.config;

import com.web.registration.entity.Role;
import com.web.registration.entity.User;
import com.web.registration.repository.RoleRepository;
import com.web.registration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.username:localadmin}")
    private String adminUsername;

    @Value("${app.default-admin.password:Admin@123456}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initRoles();
        initLocalAdmin();
    }

    private void initRoles() {
        List.of("LOCAL_ADMIN", "ADMIN", "LECTURER", "STUDENT").forEach(name -> {
            if (!roleRepository.existsByName(name)) {
                roleRepository.save(Role.builder().name(name).build());
            }
        });
    }

    private void initLocalAdmin() {
        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }
        Role localAdminRole = roleRepository.findByName("LOCAL_ADMIN")
                .orElseThrow(() -> new IllegalStateException("Role LOCAL_ADMIN not found after init"));

        userRepository.save(User.builder()
                .username(adminUsername)
                .email("localadmin@system.local")
                .passwordHash(passwordEncoder.encode(adminPassword))
                .fullName("Local Administrator")
                .role(localAdminRole)
                .build());
    }
}
