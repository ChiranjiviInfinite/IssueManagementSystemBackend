package com.example.IMS.user.controller;

import com.example.IMS.user.dto.LoginRequest;
import com.example.IMS.user.dto.RegisterRequest;
import com.example.IMS.user.dto.UserResponse;
import com.example.IMS.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = userService.registerUser(registerRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        userService.validateUser(loginRequest);

        var user = userService.findByUsername(loginRequest.getUsername());

        UserResponse userResponse = userService.convertToResponse(user);

        return ResponseEntity.ok(userResponse);
    }

}
