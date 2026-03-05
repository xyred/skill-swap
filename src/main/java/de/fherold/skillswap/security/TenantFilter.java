package de.fherold.skillswap.security;

import de.fherold.skillswap.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Ask Spring Security: "Who is currently logged in?"
        // This is the "Pocket" where Step 1 stored the user's ID Badge
        org.springframework.security.core.Authentication auth =
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        // 2. If the user is logged in, grab their specific Tenant ID
        if (auth != null && auth.getPrincipal() instanceof TenantUserDetails user) {
            String tenantId = user.getTenantId();

            // 3. Put it in our ThreadLocal "Locker" (TenantContext)
            TenantContext.setTenantId(tenantId);
        }

        try {
            // 4. Continue with the request
            filterChain.doFilter(request, response);
        } finally {
            // 5. CRITICAL: Wipe the locker clean so the next request starts fresh
            TenantContext.clear();
        }
    }
}
