package ru.example.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.authservice.dto.AuthResponse;
import ru.example.authservice.dto.LoginRequest;

import javax.naming.AuthenticationException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

          @Autowired
          private AuthenticationManager authenticationManager;

          @PostMapping("/login")
          public ResponseEntity<?> login(@RequestBody LoginRequest request) {
                    // Аутентифицируем пользователя
                    Authentication authentication = authenticationManager.authenticate(
                              new UsernamePasswordAuthenticationToken(
                                        request.getUsername(),
                                        request.getPassword()
                              )
                    );

                    // Если аутентификация успешна - генерируем ответ
                    AuthResponse response = new AuthResponse(
                              "jwt-token-" + System.currentTimeMillis(),
                              "Development",
                              request.getUsername() + "@example.com",
                              3600,
                              request.getUsername(),
                              "USER",
                              "Bearer",
                              1L,
                              request.getUsername()
                    );

                    return ResponseEntity.ok(response);

          }
}