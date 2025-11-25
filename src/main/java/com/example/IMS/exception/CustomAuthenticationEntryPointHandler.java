package com.example.IMS.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String json = """
        {
            "timestamp": "%s",
            "status": 401,
            "error": "Unauthorized",
            "message": "User is not logged in",
            "path": "%s"
        }
        """.formatted(
                java.time.LocalDateTime.now().toString(),
                request.getRequestURI());

        response.getWriter().write(json);
    }
}
