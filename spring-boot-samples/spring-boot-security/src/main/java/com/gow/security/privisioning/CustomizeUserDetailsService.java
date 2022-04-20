package com.gow.security.privisioning;

import com.gow.security.jwt.JWTUser;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/8/2 0002
 */
@Component
public class CustomizeUserDetailsService implements UserDetailsManager, UserDetailsPasswordService {

    private final Map<String, MutableUser> users = new HashMap<>();


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = this.users.get(username.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    /**
     * 密码更新是处理
     *
     * @param user
     * @param newPassword
     * @return
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        String username = user.getUsername();
        MutableUser jwtUser = this.users.get(username.toLowerCase());
        jwtUser.setPassword(newPassword);
        return jwtUser;
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    public void addUser(JWTUser userDetails) {

        users.put(userDetails.getUsername(), new MutableUser(userDetails));
    }
}
