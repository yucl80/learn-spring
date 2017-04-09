package yucl.learn.demo.security;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.security.auth.login.Configuration;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by YuChunlei on 2017/4/7.
 * http://stackoverflow.com/questions/8381776/dynamic-spring-security-using-sql-query
 */
@Component
public class JdbcFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private List<FilterInvocationSecurityMetadataSource> filterInvocationSecurityMetadataSourceList = new ArrayList<>();
    private  Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

    public void addFilterInvocationSecurityMetadataSource(FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource) {
        filterInvocationSecurityMetadataSourceList.add(filterInvocationSecurityMetadataSource);
    }

    public  JdbcFilterInvocationSecurityMetadataSource(){
              //resourceMap.put("/hello",SecurityConfig.createList(new String[] { "ROLE_ADMIN" ,"ROLE_USER"}));
         resourceMap.put("/user",SecurityConfig.createList("ROLE_USER"));

        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
        requestMap.put(new AntPathRequestMatcher("/opr"),SecurityConfig.createList("hasRole('ROLE_OPR') or hasRole('ROLE_ADMIN')"));
        SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();
        filterInvocationSecurityMetadataSourceList.add(new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap,expressionHandler));

    }

    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;

        //String url = filterInvocation.getRequestUrl();
       // HttpServletRequest request = filterInvocation.getHttpRequest();

        // Instead of hard coding the roles lookup the roles from the database using the url and/or HttpServletRequest
        // Do not forget to add caching of the lookup

        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String url = ite.next();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
            if(requestMatcher.matches(filterInvocation.getHttpRequest())) {
                configAttributes.addAll(resourceMap.get(url));
                break;
            }
        }

        for(FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource : filterInvocationSecurityMetadataSourceList){
            Collection<ConfigAttribute> ss = filterInvocationSecurityMetadataSource.getAttributes(object);
            if(ss != null){
                configAttributes.addAll(ss);
            }
        }

        return configAttributes;

    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
