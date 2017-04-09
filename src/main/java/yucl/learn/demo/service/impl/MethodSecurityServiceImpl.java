package yucl.learn.demo.service.impl;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import yucl.learn.demo.service.MethodSecurityService;

/**
 * Created by YuChunlei on 2017/4/7.
 */

@Service
public class MethodSecurityServiceImpl implements MethodSecurityService {
    @Override
    public String requiresUserRole() {
        return "You have ROLE_USER";
    }

    @Override
    public String requiresAdminRole() {
        return "You have ROLE_ADMIN";
    }
}
