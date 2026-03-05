package de.fherold.skillswap.config;

import de.fherold.skillswap.security.TenantFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // We pass this in via the Bean method below to keep it clean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TenantFilter tenantFilter) throws Exception {
        http
            // 1. Disable CSRF (standard for development/APIs)
            .csrf(AbstractHttpConfigurer::disable)

            // 2. The CRITICAL Part: Basic Auth happens, THEN we run our Tenant Filter
            // We use BasicAuthenticationFilter.class because you are using .httpBasic()
            .addFilterAfter(tenantFilter, BasicAuthenticationFilter.class)

            // 3. H2-Console and Swagger support
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

            // 4. Permissions
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
            )

            // 5. Use Basic Auth (populates the SecurityContext)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Plain text for now - perfect for testing data.sql
        return NoOpPasswordEncoder.getInstance();
    }
}
