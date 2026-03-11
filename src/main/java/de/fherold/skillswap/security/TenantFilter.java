package de.fherold.skillswap.security;

import de.fherold.skillswap.model.User;
import de.fherold.skillswap.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get the current authentication from Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Check if the user is authenticated and is one of our User entities
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            // 3. Set the tenant in our ThreadLocal context
            // This is what the @Tenant aspect or Hibernate filter will read
            TenantContext.setTenantId(user.getTenantId());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 4. CRITICAL: Always clear the context after the request is finished
            // This prevents "Tenant Leaks" between different user requests
            TenantContext.clear();
        }
    }
}
