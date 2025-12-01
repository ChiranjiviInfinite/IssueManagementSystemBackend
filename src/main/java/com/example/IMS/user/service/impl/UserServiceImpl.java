package com.example.IMS.user.service.impl;

import com.example.IMS.jwt.service.JWTService;
import com.example.IMS.user.dto.LoginRequest;
import com.example.IMS.user.dto.RegisterRequest;
import com.example.IMS.user.dto.UserResponse;
import com.example.IMS.user.enums.RoleType;
import com.example.IMS.exception.InvalidOperationException;
import com.example.IMS.exception.ResourceNotFoundException;
import com.example.IMS.user.model.Role;
import com.example.IMS.user.model.User;
import com.example.IMS.user.repository.RoleRepository;
import com.example.IMS.user.repository.UserRepository;
import com.example.IMS.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    @Override
    @Transactional
    public UserResponse registerUser(RegisterRequest registerRequest) {

        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new InvalidOperationException("Username already exists");
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new InvalidOperationException("Email already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        Role userRole = roleRepository.findByName(RoleType.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: USER"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Override
    public String validateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateToken(authentication);
    }

    public UserResponse convertToResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return response;
    }
}
