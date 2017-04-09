package yucl.learn.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yucl.learn.demo.service.MethodSecurityService;

import java.security.Principal;
import java.util.Date;

/**
 * Created by YuChunlei on 2017/4/1.
 */
@RestController
public class TestController {

    @Autowired
    MethodSecurityService methodSecurityService;


    @RequestMapping("/user")
    public String hello(Principal principal){
        return "user " +methodSecurityService.requiresUserRole()+"   "+ new Date()+"   "+ principal.toString();
    }

    @RequestMapping("/admin")
    public String admin(Principal principal){
        return "admin " +methodSecurityService.requiresAdminRole()+"   "+ new Date()+"   "+ principal.toString();
    }

    @RequestMapping("/opr")
    public String test(Principal principal){
        return "opr " + new Date() +"   "+ principal.toString();
    }


    @RequestMapping("/test")
    public String a(Principal principal){
        return  principal.toString();
    }


}
