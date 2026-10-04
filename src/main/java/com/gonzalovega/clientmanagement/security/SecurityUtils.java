package com.gonzalovega.clientmanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SecurityUtils {

    /**
     * Retrieves the current JWT token from the Security Context.
     *
     * @return the current JWT token if available, or null if no authentication is present
     */
    public String getCurrentToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? (String) auth.getCredentials() : null;
    }

    /**
     * Retrieves the current authenticated username from the Security Context.
     *
     * @return the current username if available, or "SYSTEM" if no authentication is present
     */
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Map<?, ?> details) {
            return (String) details.get("username");
        }
        return "SYSTEM";
    }
}