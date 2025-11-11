package ru.example.authservice.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import ru.example.authservice.security.CustomUserDetailsService;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

          private static KeyPair generateRsaKey() throws NoSuchAlgorithmException {
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                    keyPairGenerator.initialize(2048);
                    return keyPairGenerator.generateKeyPair();
          }

          @Bean
          @Order(1)
          public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
                    http
                              .securityMatcher("/api/**")  // Только API endpoints
                              .authorizeHttpRequests(authz -> authz

                                        .anyRequest().permitAll()  // Разрешаем все API запросы
                              )
                              .csrf(csrf -> csrf.disable())
                              .sessionManagement(session -> session
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                              );

                    return http.build();
          }

          @Bean
          @Order(2)
          public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
                    throws Exception {
                    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

                    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                              .oidc(Customizer.withDefaults());


                    http.oauth2Client(Customizer.withDefaults());

                    http.formLogin(form -> form
                              .loginPage("/login")
                              .loginProcessingUrl("/login")
                              .successHandler((request, response, authentication) -> {
                                        System.out.println("=== LOGIN SUCCESS ===");
                                        String originalRequest = (String) request.getSession()
                                                  .getAttribute("ORIGINAL_OAUTH2_REQUEST");
                                        if (originalRequest != null) {
                                                  response.sendRedirect(originalRequest);
                                        } else {
                                                  response.sendRedirect("/");
                                        }
                              })
                              .failureUrl("/login?error=true")
                              .permitAll()
                    );

                    http.exceptionHandling(handling -> handling
                              .authenticationEntryPoint((request, response, authException) -> {
                                        response.sendRedirect("/login");
                              })
                    );

                    return http.build();
          }

          @Bean
          @Order(3)
          public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
                    throws Exception {
                    http
                              .authorizeHttpRequests(authz -> authz
                                        .requestMatchers("/", "/login", "/error", "/webjars/**",
                                                  "/css/**", "/js/**", "/images/**").permitAll()
                                        .anyRequest().authenticated()
                              )
                              .csrf(csrf -> csrf.disable());

                    return http.build();
          }

          @Bean
          public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
                    JdbcRegisteredClientRepository jdbcRepository =
                              new JdbcRegisteredClientRepository(jdbcTemplate);

                    initializeDefaultClients(jdbcRepository);

                    return jdbcRepository;
          }

          private void initializeDefaultClients(JdbcRegisteredClientRepository jdbcRepository) {
                    if (jdbcRepository.findByClientId("gateway-client") == null) {
                              RegisteredClient gatewayClient =
                                        RegisteredClient.withId(UUID.randomUUID().toString())
                                                  .clientId("gateway-client")
                                                  .clientSecret("{noop}gateway-secret")
                                                  .clientAuthenticationMethod(
                                                            ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                                                  .authorizationGrantType(
                                                            AuthorizationGrantType.AUTHORIZATION_CODE)
                                                  .authorizationGrantType(
                                                            AuthorizationGrantType.REFRESH_TOKEN)
                                                  .redirectUri(
                                                            "http://localhost:8080/login/oauth2/code/auth-service")
                                                  .scope("read")
                                                  .scope("write")
                                                  .tokenSettings(TokenSettings.builder()
                                                            .accessTokenTimeToLive(
                                                                      Duration.ofHours(1))
                                                            .build())
                                                  .clientSettings(ClientSettings.builder()
                                                            .requireAuthorizationConsent(false)
                                                            .build())
                                                  .build();

                              jdbcRepository.save(gatewayClient);
                    }
          }


          @Bean
          public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
                    KeyPair keyPair = generateRsaKey();
                    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
                    RSAKey rsaKey = new RSAKey.Builder(publicKey)
                              .privateKey(privateKey)
                              .keyID(UUID.randomUUID().toString())
                              .build();
                    JWKSet jwkSet = new JWKSet(rsaKey);
                    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
          }

          @Bean
          public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
                    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
          }


          @Bean
          public AuthenticationManager authenticationManager(
                    CustomUserDetailsService userDetailsService,
                    PasswordEncoder passwordEncoder) {
                    DaoAuthenticationProvider authenticationProvider =
                              new DaoAuthenticationProvider();
                    authenticationProvider.setUserDetailsService(userDetailsService);
                    authenticationProvider.setPasswordEncoder(passwordEncoder);

                    return new ProviderManager(authenticationProvider);
          }

          @Bean
          public PasswordEncoder passwordEncoder() {
                    return new BCryptPasswordEncoder();
          }

          @Bean
          public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
                    return new NimbusJwtEncoder(jwkSource);
          }
}