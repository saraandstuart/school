package com.shannoncode.school.common.security.config;

import com.shannoncode.school.common.security.converter.ProfileSyncConverter;
import com.shannoncode.school.common.security.handler.CustomAccessDeniedHandler;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Used only for OpenAPI generation (Gradle springdoc task).
     */
    @Bean
    @Order(0)
    @Profile("openapi")
    public SecurityFilterChain openApiSecurityFilterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api-docs",
                    "/api-docs/**",
                    "/api-docs.yaml",
                    "/api-docs.yml",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                ).permitAll()
                .anyRequest().permitAll()
            )
            // Don't configure oauth2ResourceServer() here on purpose.
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Standard Security Filter Chain for Runtime (not openapi profile).
     */
    @Bean
    @Order(1)
    @Profile("!openapi")
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        ProfileSyncConverter profileSyncConverter,
        CustomAccessDeniedHandler customAccessDeniedHandler,
        CorsConfigurationSource corsConfigurationSource
    ) {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/course/**").permitAll()
                .requestMatchers("/api/v1/course/**").hasRole("ADMIN")
                .requestMatchers(
                    "/api-docs",
                    "/api-docs/**",
                    "/api-docs.yaml",
                    "/api-docs.yml",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                ).permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler(customAccessDeniedHandler)
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(profileSyncConverter))
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://<yourapp.com>"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        configuration.setExposedHeaders(List.of("Location"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
