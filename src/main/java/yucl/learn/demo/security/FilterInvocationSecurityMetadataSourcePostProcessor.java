package yucl.learn.demo.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Created by YuChunlei on 2017/4/7.
 */

@Component
public class FilterInvocationSecurityMetadataSourcePostProcessor implements BeanPostProcessor, InitializingBean {

    @Autowired
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    public Object postProcessAfterInitialization(Object bean, String name) {
        if (bean instanceof FilterSecurityInterceptor) {
            FilterInvocationSecurityMetadataSource defaultSecurityMetadataSource = ((FilterSecurityInterceptor) bean).getSecurityMetadataSource();
            ((JdbcFilterInvocationSecurityMetadataSource) securityMetadataSource).addFilterInvocationSecurityMetadataSource(defaultSecurityMetadataSource);
            ((FilterSecurityInterceptor) bean).setSecurityMetadataSource(securityMetadataSource);
        }
        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String name) {
        return bean;
    }

    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(securityMetadataSource, "securityMetadataSource cannot be null");
    }
}
