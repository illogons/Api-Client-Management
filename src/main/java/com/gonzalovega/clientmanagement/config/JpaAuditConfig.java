package com.gonzalovega.clientmanagement.config;

import lombok.RequiredArgsConstructor;
import com.gonzalovega.clientmanagement.security.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@RequiredArgsConstructor
public class JpaAuditConfig {

    private final SecurityUtils securityUtils;

    /**
     * Bean definition for AuditorAware that provides the current authenticated username for JPA auditing.
     *
     * - Retrieves the current username from the SecurityUtils component, which accesses the Security Context.
     * - Returns an Optional containing the username, or "SYSTEM" if no authentication is present.
     *
     * @return an AuditorAware instance that provides the current username for auditing purposes
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(securityUtils.getCurrentUsername());
    }
}