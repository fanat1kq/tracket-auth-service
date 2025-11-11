package ru.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

          private final JwtEncoder jwtEncoder;

          public String generateJwtToken(Authentication authentication) {
                    Instant now = Instant.now();
                    long expiry = 3600L;

                    JwtClaimsSet claims = JwtClaimsSet.builder()
                              .issuer("auth-service")
                              .issuedAt(now)
                              .expiresAt(now.plusSeconds(expiry))
                              .subject(authentication.getName())
                              .claim("username", authentication.getName())
                              .claim("scope", "read write")
                              .build();

                    JwtEncoderParameters parameters = JwtEncoderParameters.from(
                              JwsHeader.with(SignatureAlgorithm.RS256).build(),
                              claims
                    );

                    return jwtEncoder.encode(parameters).getTokenValue();
          }
}
