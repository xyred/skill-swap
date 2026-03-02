package de.fherold.skillswap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF for H2-Console and API development
            .csrf(AbstractHttpConfigurer::disable)

            // 2. Allow H2-Console to display in frames
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

            // 3. Define access rules
            .authorizeHttpRequests(auth -> auth
                // Whitelist Swagger and H2
                .requestMatchers("/h2-console/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                // Lock down everything else
                .anyRequest().authenticated()
            )

            // 4. Enable a simple login for now (Basic Auth)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
