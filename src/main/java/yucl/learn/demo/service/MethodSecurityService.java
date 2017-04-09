package yucl.learn.demo.service;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by YuChunlei on 2017/4/7.
 */
public interface MethodSecurityService {

    @PreAuthorize("hasRole('ROLE_USER')")
    String requiresUserRole();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String requiresAdminRole();
}
