package ru.example.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.example.authservice.dto.request.UserRequestDTO;
import ru.example.authservice.dto.response.UserResponseDTO;
import ru.example.authservice.entity.User;
import ru.example.authservice.security.CustomUserDetails;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

//    UserResponseDTO toDto(CustomUserDetails userDetails);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "authorities", ignore = true)
    CustomUserDetails toCustomUserDetails(User user);

    default User toEntityWithPassword(UserRequestDTO dto, PasswordEncoder passwordEncoder) {

        User user = toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        return user;
    }

    UserResponseDTO toAuthDto(CustomUserDetails userDetails, String token);
}