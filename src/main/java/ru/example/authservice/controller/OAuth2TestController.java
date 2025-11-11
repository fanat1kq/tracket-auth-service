package ru.example.authservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OAuth2TestController {

          @GetMapping("/oauth2/authorization/google")
          public void testGoogleOAuth(HttpServletResponse response) throws IOException {
                    System.out.println("=== OAuth2 Google endpoint called ===");
                    // Временный redirect на Google OAuth (замените на реальный client-id)
                    String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                              "client_id=your-client-id&" +
                              "redirect_uri=http://localhost:9000/login/oauth2/code/google&" +
                              "response_type=code&" +
                              "scope=email%20profile";
                    response.sendRedirect(googleAuthUrl);
          }
}
