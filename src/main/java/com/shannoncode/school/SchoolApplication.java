package com.shannoncode.school;

import com.shannoncode.school.component.CustomAccessDeniedHandler;
import com.shannoncode.school.component.ProfileSyncConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class SchoolApplication {

    static void main(String[] args) {
        SpringApplication.run(SchoolApplication.class, args);
    }

    /**
     * Used only for OpenAPI generation (Gradle springdoc task).
     * - Permits api docs + swagger UI endpoints
     * - Avoids requiring an OAuth2 issuer to be reachable during build
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

    @Bean
    @Order(1)
    @Profile("!openapi")
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ProfileSyncConverter profileSyncConverter) {
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
                .accessDeniedHandler(new CustomAccessDeniedHandler())
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(profileSyncConverter))
            );
        return http.build();
    }

}
