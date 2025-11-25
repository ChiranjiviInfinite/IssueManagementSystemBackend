package com.example.IMS.initializer;

import com.example.IMS.user.enums.RoleType;
import com.example.IMS.user.model.Role;
import com.example.IMS.user.model.User;
import com.example.IMS.user.repository.RoleRepository;
import com.example.IMS.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        log.info("Initializing sample data...");

        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName(RoleType.ADMIN);
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName(RoleType.USER);
            roleRepository.save(userRole);

            log.info("Roles created: ADMIN, USER");
        }

        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName(RoleType.ADMIN).orElseThrow();
            Role userRole = roleRepository.findByName(RoleType.USER).orElseThrow();

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            admin.setRoles(adminRoles);
            userRepository.save(admin);

            User user1 = new User();
            user1.setUsername("Chiranjivi");
            user1.setPassword(passwordEncoder.encode("chiranjivi123"));
            user1.setEmail("chiranjivi@example.com");
            Set<Role> user1Roles = new HashSet<>();
            user1Roles.add(userRole);
            user1.setRoles(user1Roles);
            userRepository.save(user1);

            User user2 = new User();
            user2.setUsername("Apar");
            user2.setPassword(passwordEncoder.encode("apar123"));
            user2.setEmail("apar@example.com");
            Set<Role> user2Roles = new HashSet<>();
            user2Roles.add(userRole);
            user2.setRoles(user2Roles);
            userRepository.save(user2);

            log.info("Users created: admin, Chiranjivi, Apar");

        }
    }
}
