package com.DTISE.ShelfMasterBE.infrastructure.config;

import com.DTISE.ShelfMasterBE.infrastructure.auth.filters.TokenBlacklist;
import com.DTISE.ShelfMasterBE.usecase.auth.GetUserAuthDetailsUsecase;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtConfigProperties jwtConfigProperties;
    private final PasswordEncoder passwordEncoder;
    private final GetUserAuthDetailsUsecase getUserAuthDetailsUsecase;
//    private final CustomUserDetailsService customUserDetailsService;
    private final TokenBlacklist tokenBlacklistFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(new CorsConfigurationSourceImpl()))
                .authorizeHttpRequests(auth -> auth
                        //  Define public routes
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/api/v1/simple").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/google-login").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/verify").permitAll()
                        .requestMatchers("/api/v1/auth/setup-account").permitAll()
                        .requestMatchers("/api/v1/auth/forgot-password").permitAll()
                        .requestMatchers("/api/v1/auth/setup-forgotten-password").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/event").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/event/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/review/average_stars").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/review").permitAll()
                        //  Define rest of the routes to be private
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder())))
                .addFilterAfter(tokenBlacklistFilter, BearerTokenAuthenticationFilter.class)
                .userDetailsService(getUserAuthDetailsUsecase)
                .build();
    }

    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(getUserAuthDetailsUsecase);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey originalKey = new SecretKeySpec(jwtConfigProperties.getSecret().getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(originalKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        SecretKey key = new SecretKeySpec(jwtConfigProperties.getSecret().getBytes(), "HmacSHA256");
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<SecurityContext>(key);
        return new NimbusJwtEncoder(immutableSecret);
    }
}
