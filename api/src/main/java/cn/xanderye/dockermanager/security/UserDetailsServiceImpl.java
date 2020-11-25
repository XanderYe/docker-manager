package cn.xanderye.dockermanager.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created on 2020/8/8.
 *
 * @author XanderYe
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Value("${user.username}")
    private String username;

    @Value("${user.password}")
    private String password;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails userDetails = new CustomUserDetails();
        if (!this.username.equals(username)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        userDetails.setUsername(username);
        userDetails.setPassword(password);
        return userDetails;
    }


}
