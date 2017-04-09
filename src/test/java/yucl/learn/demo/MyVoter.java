package yucl.learn.demo;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

/**
 * http://stackoverflow.com/questions/8321696/creating-new-roles-and-permissions-dynamically-in-spring-security-3?noredirect=1&lq=1
  * Created by YuChunlei on 2017/3/31.
 */
@Service
public class MyVoter implements AccessDecisionVoter<Object> {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        //return clazz == Project.class;

        return  true;
    }

    @Override
    public int vote(Authentication authentication, Object someEntity,
                    Collection<ConfigAttribute> configs) {

            FilterInvocation filterInvocation = (FilterInvocation) someEntity;

        for (ConfigAttribute config : configs) {
            if (supports(config)) { // Add this check
                if(config instanceof SecurityConfig) {
                    String needRole = ((SecurityConfig) config).getAttribute();
                    for (GrantedAuthority ga : authentication.getAuthorities()) {
                        if (needRole.equals(ga.getAuthority())) { //ga is user's role.
                            return ACCESS_GRANTED;
                        }
                    }
                }
            } else {
                return ACCESS_DENIED; // Abstain Based on your requirement
            }
        }
        return ACCESS_DENIED;

       /* return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(r -> "ROLE_USER".equals(r)
                        && LocalDateTime.now().getMinute() % 2 != 0)
                .findAny()
                .map(s -> ACCESS_DENIED)
                .orElseGet(() -> ACCESS_ABSTAIN);*/


        /*if(configs==null){
            return ACCESS_DENIED;
        }
        System.out.println(someEntity.toString()); //object is a URL.
        Iterator<ConfigAttribute> ite=configs.iterator();
        while(ite.hasNext()) {
            ConfigAttribute ca = ite.next();
            String needRole = ((SecurityConfig) ca).getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needRole.equals(ga.getAuthority())) { //ga is user's role.
                    return ACCESS_GRANTED;

                }
            }
        }*/
    }

}