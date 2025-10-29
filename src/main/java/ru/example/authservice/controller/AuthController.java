package ru.example.authservice.controller;

import lombok.RequiredArgsConstructor;
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
import ru.example.authservice.dto.request.UserRequestDTO;
import ru.example.authservice.entity.User;
import ru.example.authservice.service.UserService;

import javax.naming.AuthenticationException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


          private final AuthenticationManager authenticationManager;
          private final UserService userService;

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

          @PostMapping("/register")
          public User register(@RequestBody UserRequestDTO request) {
                    return userService.signUp(request);

          }
}