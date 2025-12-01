package com.example.IMS.user.service;

import com.example.IMS.user.dto.LoginRequest;
import com.example.IMS.user.dto.RegisterRequest;
import com.example.IMS.user.dto.UserResponse;
import com.example.IMS.user.model.User;

public interface UserService {

    UserResponse registerUser(RegisterRequest registerRequest);

    User findByUsername(String username);

    String validateUser(LoginRequest loginRequest);

    UserResponse convertToResponse(User user);
}
