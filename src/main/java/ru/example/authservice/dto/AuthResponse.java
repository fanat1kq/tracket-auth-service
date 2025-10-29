package ru.example.authservice.dto;

import org.springframework.security.oauth2.jwt.DPoPProofContext;

public class AuthResponse {
          private String accessToken;
          private String tokenType;
          private Long userId;
          private String username;
          private String email;
          private String name;
          private String role;
          private String department;
          private Integer expiresIn;

          public AuthResponse(String accessToken, String department, String email,
                              Integer expiresIn,
                              String name, String role, String tokenType, Long userId,
                              String username) {
                    this.accessToken = accessToken;
                    this.department = department;
                    this.email = email;
                    this.expiresIn = expiresIn;
                    this.name = name;
                    this.role = role;
                    this.tokenType = tokenType;
                    this.userId = userId;
                    this.username = username;
          }

          public String getAccessToken() {
                    return accessToken;
          }

          public void setAccessToken(String accessToken) {
                    this.accessToken = accessToken;
          }

          public String getDepartment() {
                    return department;
          }

          public void setDepartment(String department) {
                    this.department = department;
          }

          public String getEmail() {
                    return email;
          }

          public void setEmail(String email) {
                    this.email = email;
          }

          public Integer getExpiresIn() {
                    return expiresIn;
          }

          public void setExpiresIn(Integer expiresIn) {
                    this.expiresIn = expiresIn;
          }

          public String getName() {
                    return name;
          }

          public void setName(String name) {
                    this.name = name;
          }

          public String getRole() {
                    return role;
          }

          public void setRole(String role) {
                    this.role = role;
          }

          public String getTokenType() {
                    return tokenType;
          }

          public void setTokenType(String tokenType) {
                    this.tokenType = tokenType;
          }

          public Long getUserId() {
                    return userId;
          }

          public void setUserId(Long userId) {
                    this.userId = userId;
          }

          public String getUsername() {
                    return username;
          }

          public void setUsername(String username) {
                    this.username = username;
          }
}

