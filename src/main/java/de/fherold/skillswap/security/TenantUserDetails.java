package de.fherold.skillswap.security;

import de.fherold.skillswap.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class TenantUserDetails extends org.springframework.security.core.userdetails.User {

    private final String tenantId;

    public TenantUserDetails(User user) {
        super(user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
        this.tenantId = user.getTenantId();
    }

    public String getTenantId() {
        return tenantId;
    }
}
