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

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Extract the ID from the custom header
        String tenantId = request.getHeader(TENANT_HEADER);

        // 2. If present, put it in our ThreadLocal "pocket"
        if (tenantId != null && !tenantId.trim().isEmpty()) {
            TenantContext.setTenantId(tenantId);
        }

        try {
            // 3. Let the request continue to the next filter/controller
            filterChain.doFilter(request, response);
        } finally {
            // 4. CRITICAL: Clear the pocket after the request is done
            // This prevents "data leaking" to the next user using this thread
            TenantContext.clear();
        }
    }
}
