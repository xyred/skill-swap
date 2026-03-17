package de.fherold.skillswap.context;

/**
 * Utility class to store the current tenant (recruiter) ID in a thread-safe way.
 * This allows us to access the tenant ID throughout the application without passing it explicitly through method parameters.
 * The tenant ID is set in the JwtAuthenticationFilter when a user successfully authenticates, and cleared after the request is processed to prevent memory leaks.
 */
public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
