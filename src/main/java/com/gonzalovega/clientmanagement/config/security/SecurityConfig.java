package com.gonzalovega.clientmanagement.config.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import com.gonzalovega.clientmanagement.config.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // Define a bean for password encoding using BCrypt, which is a strong hashing algorithm for securely storing passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for the application.
     * - Disables CSRF protection and enables CORS with a custom configuration.
     * - Sets session management to stateless since JWT is used for authentication.
     * - Permits all requests to Swagger UI and API documentation endpoints, while requiring authentication for all other requests.
     * - Configures a custom authentication entry point to handle unauthorized access attempts.
     * - Adds the JWT authentication filter before the UsernamePasswordAuthenticationFilter in the filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @return a SecurityFilterChain that defines the security configuration for the application
     * @throws Exception if an error occurs while configuring security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Defines a custom AuthenticationEntryPoint to handle unauthorized access attempts.
     * - Sets the HTTP response status to 401 Unauthorized.
     * - Returns a JSON response with an error message indicating that the token is missing or invalid.
     *
     * @return an AuthenticationEntryPoint that handles unauthorized access attempts
     */
    private AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Token missing or invalid.\"}");
        };
    }

    /**
     * Configures CORS settings for the application.
     * - Allows requests from specified origins defined in the application properties.
     * - Permits common HTTP methods such as GET, POST, PUT, DELETE, and OPTIONS.
     * - Allows specific headers like Authorization and Content-Type.
     * - Enables credentials to be included in CORS requests.
     *
     * @return a CorsConfigurationSource that defines the CORS configuration for the application
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList(
                "https://*",
                "http://*"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Origin"
        ));

        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}