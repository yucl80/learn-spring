package yucl.learn.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class LearnSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnSpringApplication.class, args);
	}

    @Autowired
    AccessDecisionManager affirmativeBased;

    @Autowired
    AuthenticationManager authenticationManager;


    public FilterChainProxy getFilterChainProxy(){
        List<SecurityFilterChain> securityFilterChainList = new ArrayList<>();
        securityFilterChainList.add(new DefaultSecurityFilterChain( new AntPathRequestMatcher("/images/*"),new ArrayList<Filter>()));
        securityFilterChainList.add(new DefaultSecurityFilterChain( new AntPathRequestMatcher("/resources/**"),new ArrayList<Filter>()));
         List<Filter> filters =  new ArrayList<>();

       // securityFilterChainList.add(new DefaultSecurityFilterChain( new AntPathRequestMatcher("/**"),"securityContextPersistenceFilter,logoutFilter,basicAuthenticationFilter,exceptionTranslationFilter,filterSecurityInterceptor"));
       return new FilterChainProxy(securityFilterChainList);
    }







}



