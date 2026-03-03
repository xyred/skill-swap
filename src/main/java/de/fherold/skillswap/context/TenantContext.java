package de.fherold.skillswap.context;

/**
 * Utility class to store the current tenant (recruiter) ID in a thread-safe way.
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
