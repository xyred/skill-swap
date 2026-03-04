package de.fherold.skillswap.aspect;

import de.fherold.skillswap.context.TenantContext;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantAspect {

    @Autowired
    private EntityManager entityManager;

    // This Pointcut tells Spring to watch ALL methods in your repository package
    @Pointcut("execution(* de.fherold.skillswap.repository.*.*(..))")
    public void repositoryMethods() {
    }

    // This "Before" advice runs right BEFORE the repository method executes
    @Before("repositoryMethods()")
    public void beforeExecution() {
        String tenantId = TenantContext.getTenantId();

        if (tenantId != null) {
            // Unwrap the Hibernate Session from the JPA EntityManager
            Session session = entityManager.unwrap(Session.class);

            // Enable the filter we defined in our Entities
            session.enableFilter("tenantFilter")
                .setParameter("tenantId", tenantId);
        }
    }
}
