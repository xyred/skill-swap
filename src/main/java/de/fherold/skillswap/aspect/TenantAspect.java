package de.fherold.skillswap.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import de.fherold.skillswap.context.TenantContext;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class TenantAspect {

    private final EntityManager entityManager;

    @Pointcut("execution(* de.fherold.skillswap.repository.*.*(..))")
    public void repositoryMethods() {
    }

    @Before("repositoryMethods()")
    public void beforeExecution() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 1. Check for Super Admin status
        if (auth != null && auth.isAuthenticated()) {
            boolean isSuperAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));

            if (isSuperAdmin) {
                System.out.println("DEBUG: Super Admin detected. Accessing global data.");
                return;
            }
        }

        // 2. Standard Multi-Tenant Logic
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter")
                   .setParameter("tenantId", tenantId);
        }
    }
}
