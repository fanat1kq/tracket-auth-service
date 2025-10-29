package ru.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.authservice.dto.AuthResponse;
import ru.example.authservice.dto.LoginRequest;
import ru.example.authservice.dto.request.UserRequestDTO;
import ru.example.authservice.entity.User;
import ru.example.authservice.repository.UserRepository;
import ru.example.authservice.service.UserService;

import javax.naming.AuthenticationException;
import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

          private final AuthenticationManager authenticationManager;
          private final UserService userService;
          private final JwtEncoder jwtEncoder; // ← Добавьте это
          private final UserRepository userRepository;

          @PostMapping("/login")
          public ResponseEntity<?> login(@RequestBody LoginRequest request) {
                    // Аутентифицируем пользователя
                    Authentication authentication = authenticationManager.authenticate(
                              new UsernamePasswordAuthenticationToken(
                                        request.getUsername(),
                                        request.getPassword()
                              )
                    );

                    // Генерируем НАСТОЯЩИЙ JWT токен
                    String jwtToken = generateJwtToken(authentication);

                    // Получаем информацию о пользователе
                    User user = userRepository.findUserByUsername(request.getUsername())
                              .orElseThrow(() -> new RuntimeException("User not found"));

                    AuthResponse response = new AuthResponse(
                              jwtToken, // ← НАСТОЯЩИЙ JWT
                              user.getUsername(),
                              "user@mail.ru",
                              3600,
                              user.getUsername(),
                              "ADMIN",
                              "Bearer",
                              user.getId(),
                              user.getUsername()
                    );

                    return ResponseEntity.ok(response);

          }

          private String generateJwtToken(Authentication authentication) {
                    Instant now = Instant.now();
                    long expiry = 3600L; // 1 hour

                    // Создаем claims (данные в токене)
                    JwtClaimsSet claims = JwtClaimsSet.builder()
                              .issuer("auth-service")
                              .issuedAt(now)
                              .expiresAt(now.plusSeconds(expiry))
                              .subject(authentication.getName())
                              .claim("username", authentication.getName())
                              .claim("scope", "read write")
                              .build();

                    // Генерируем JWT
                    JwtEncoderParameters parameters = JwtEncoderParameters.from(
                              JwsHeader.with(SignatureAlgorithm.RS256).build(), // Используем RSA
                              claims
                    );

                    return jwtEncoder.encode(parameters).getTokenValue();
          }

          @PostMapping("/register")
          public User register(@RequestBody UserRequestDTO request) {
                    return userService.signUp(request);
          }
}