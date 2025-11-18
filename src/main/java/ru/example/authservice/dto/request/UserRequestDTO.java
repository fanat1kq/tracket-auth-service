package ru.example.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotNull
    @NotEmpty
    @Size(min = 5, max = 20)
    private String username;
    @Email
    private String email;
    @NotNull
    @NotEmpty
    @Size(min = 8)
    private String password;
}