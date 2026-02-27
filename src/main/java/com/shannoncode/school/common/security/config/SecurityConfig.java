package com.shannoncode.school.common.security.config;

import com.shannoncode.school.common.security.converter.ProfileSyncConverter;
import com.shannoncode.school.common.security.handler.CustomAccessDeniedHandler;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Used only for OpenAPI generation (Gradle springdoc task).
     */
    @Bean
    @Order(0)
    @Profile("openapi")
    public SecurityFilterChain openApiSecurityFilterChain(HttpSecurity http) throws Exception {
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
        CustomAccessDeniedHandler customAccessDeniedHandler
    ) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/v1/course/**").permitAll()
                .requestMatchers("/v1/course/**").hasRole("ADMIN")
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

}
