package ru.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.authservice.dto.response.AuthResponse;
import ru.example.authservice.dto.request.LoginRequest;
import ru.example.authservice.dto.UserDto;
import ru.example.authservice.dto.request.UserRequestDTO;
import ru.example.authservice.entity.User;
import ru.example.authservice.service.UserService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public AuthResponse login(@Validated @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/register")
    public User register(@Validated @RequestBody UserRequestDTO request) {
        return userService.signUp(request);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}