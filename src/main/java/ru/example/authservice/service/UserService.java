package ru.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.authservice.dto.Notification;
import ru.example.authservice.dto.NotificationType;
import ru.example.authservice.dto.RecipientType;
import ru.example.authservice.dto.request.UserRequestDTO;
import ru.example.authservice.entity.User;
import ru.example.authservice.mapper.UserMapper;
import ru.example.authservice.publisher.NotificationPublisher;
import ru.example.authservice.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

          private final UserRepository userRepository;

          private final PasswordEncoder passwordEncoder;

          private final UserDetailsService userDetailsService;

          private final UserMapper userMapper;

          private final NotificationPublisher notificationPublisher;

          @Transactional
          public User signUp(UserRequestDTO userRequestDTO) {
                    User user = userMapper.toEntityWithPassword(userRequestDTO, passwordEncoder);
                    User savedUser = userRepository.save(user);

                    Notification message = Notification
                              .builder()
                              .templateType(NotificationType.WELCOME.getTemplateName().toUpperCase())
                              .recipientType(RecipientType.EMAIL.getRecipientName())
                              .to(userRequestDTO.email())
                              .data(Collections.emptyMap())
                              .build();

                    notificationPublisher.send(message);
                    //TODO:outbox

                    return savedUser;
          }

//    @Transactional(readOnly = true)
//    public UserResponseDTO signIn(UserRequestDTO request,
//                                  HttpServletRequest httpServletRequest,
//                                  HttpServletResponse httpServletResponse) {
//
//        CustomUserDetails userDetails = (CustomUserDetails)
//                userDetailsService.loadUserByUsername(request.username());
//
//        String token = jwtTokenUtils.generateToken(userDetails);
//
//        return userMapper.toAuthDto(userDetails, token);
//    }
//
//    public void authenticateUser(User user,
//                                 HttpServletRequest httpRequest,
//                                 HttpServletResponse httpResponse) {
//        CustomUserDetails userDetails = userMapper.toCustomUserDetails(user);
//    }
}