package org.projectzero.auth.web.social;

import org.apache.commons.lang3.RandomStringUtils;
import org.projectzero.auth.lib.entity.Role;
import org.projectzero.auth.lib.entity.User;
import org.projectzero.auth.lib.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class FacebookConnectionSignUp implements ConnectionSignUp {

    @Autowired
    private AuthService authService;

    @Override
    public String execute(Connection<?> connection) {
        try {
            UserDetails userDetails = authService.loadUserByUsername(connection.getDisplayName());
            return userDetails.getUsername();
        } catch (UsernameNotFoundException unfe) {
            final User user = new User();
            user.setUsername(connection.getDisplayName());
            user.setPassword(RandomStringUtils.randomAlphabetic(8));
            user.setActive(true);
            user.setRoles(Arrays.asList(
                    authService.getOrCreateRoleIfNotPresent(Role.FACEBOOK_ROLE)
            ));
            authService.saveUser(user);
            return user.getUsername();
        }
    }
}
