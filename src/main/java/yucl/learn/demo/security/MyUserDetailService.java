package yucl.learn.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by YuChunlei on 2017/4/7.
 */
public class MyUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        GrantedAuthority auth2 = new SimpleGrantedAuthority("ROLE_ADMIN");
        auths.add(auth2);
        if (username.equals("robin1")) {
            auths = new ArrayList<GrantedAuthority>();
            GrantedAuthority auth1 = new SimpleGrantedAuthority("ROLE_ROBIN");
            auths.add(auth1);
        }

      // User(String username, String password, boolean enabled, boolean accountNonExpired,
      // boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities) {
        User user = new User(username,
                "robin", true, true, true, true, auths);
        return user;

    }
}
