package ru.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.authservice.dto.AuthResponse;
import ru.example.authservice.dto.EventType;
import ru.example.authservice.dto.LoginRequest;
import ru.example.authservice.dto.UserDto;
import ru.example.authservice.dto.UserRegisteredPayload;
import ru.example.authservice.dto.request.UserRequestDTO;
import ru.example.authservice.entity.User;
import ru.example.authservice.mapper.UserMapper;
import ru.example.authservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

          private final UserRepository userRepository;

          private final PasswordEncoder passwordEncoder;

          private final OutboxService outboxService;

          private final UserMapper userMapper;

          private final JwtTokenService jwtTokenService;

          private final AuthenticationManager authenticationManager;

          public AuthResponse login(LoginRequest request) {
                    Authentication authentication = authenticationManager.authenticate(
                              new UsernamePasswordAuthenticationToken(
                                        request.username(),
                                        request.password()
                              )
                    );

                    String jwtToken = jwtTokenService.generateJwtToken(authentication);

                    User user = userRepository.findUserByUsername(request.username())
                              .orElseThrow(() -> new RuntimeException("User not found"));

                    return userMapper.toAuthResponse(jwtToken, user);

          }

          @Transactional
          public User signUp(UserRequestDTO userRequestDTO) {
                    User user = userMapper.toEntityWithPassword(userRequestDTO, passwordEncoder);
                    User savedUser = userRepository.save(user);

                    UserRegisteredPayload payload = userMapper.toUserRegisteredPayload(savedUser);
                    outboxService.createEvent(EventType.USER_REGISTERED.getEventTypeName(), payload);

                    return savedUser;
          }

          public List<UserDto> getAllUsers() {
                    return Streamable.of(userRepository.findAll()).stream()
                              .map(userMapper::toUserDto)
                              .toList();
          }
}