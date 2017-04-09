package yucl.learn.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YuChunlei on 2017/3/31.
 * * AffirmativeBased – grants access if any of the AccessDecisionVoters return an affirmative vote
 *ConsensusBased – grants access if there are more affirmative votes than negative (ignoring users who abstain)
 *UnanimousBased – grants access if every voter either abstains or returns an affirmative vote
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled = true, proxyTargetClass = true)
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

     @Autowired
    private RoleHierarchyVoter roleHierarchyVoter;

    @Autowired
    private WebExpressionVoter webExpressionVoter;

    @Bean
    AccessDecisionVoter authenticatedVoter(){
        return  new AuthenticatedVoter();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(  authenticatedVoter(), roleHierarchyVoter,  webExpressionVoter);
        return new UnanimousBased(decisionVoters);
    }

    @Bean
    public UserDetailsService userDetailsService(JdbcTemplate jdbcTemplate) {
        RowMapper<User> userRowMapper = (ResultSet rs, int i) ->
                new User(
                        rs.getString("ACCOUNT_NAME"),
                        rs.getString("PASSWORD"),
                        rs.getBoolean("ENABLED"),
                        rs.getBoolean("ENABLED"),
                        rs.getBoolean("ENABLED"),
                        rs.getBoolean("ENABLED"),
                        AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN"));
        return username ->
                jdbcTemplate.queryForObject("SELECT * from ACCOUNT where ACCOUNT_NAME = ?",
                        userRowMapper, username);
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
       /* auth.jdbcAuthentication().dataSource(authDBDataSource).userCache(buildUserCache())
                .usersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username = ?")
                .authoritiesByUsernameQuery(
                        "select username ,role_name as authorities from user_role where username = ?");*/
        auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER").and()
                .withUser("admin").password("pass").roles("ADMIN").and()
        .withUser("opr").password("pass").roles("OPR")        ;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
                .accessDecisionManager(accessDecisionManager())
                .and()
                .formLogin().permitAll();


    }




}
