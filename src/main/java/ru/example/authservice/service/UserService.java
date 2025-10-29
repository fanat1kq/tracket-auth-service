//package ru.example.authservice.service;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ru.example.authservice.mapper.UserMapper;
//import ru.example.authservice.repository.UserRepository;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final UserDetailsService userDetailsService;
//    private final UserMapper userMapper;
//    private final JwtTokenUtils jwtTokenUtils;
//
//    @Transactional
//    public User signUp(UserRequestDTO userRequestDTO) {
//        User user = userMapper.toEntityWithPassword(userRequestDTO, passwordEncoder);
//        User savedUser = userRepository.save(user);
//
//        return savedUser;
//    }
//
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
//}