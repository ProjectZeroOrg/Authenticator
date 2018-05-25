package org.projectzero.auth.extras.initdb;

import org.projectzero.auth.lib.config.AuthLibConfig;
import org.projectzero.auth.lib.entity.Role;
import org.projectzero.auth.lib.entity.User;
import org.projectzero.auth.lib.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
@Import(AuthLibConfig.class)
public class AuthDbInitApp {

    @Value("${auth.superuser.name:superuser}")
    private String superuserName;

    @Value("${auth.superuser.pass:pass123}")
    private String superuserPass;

    @Autowired
    private AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(AuthDbInitApp.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    CommandLineRunner insertSuperUser() {
        User superUser = new User();
        superUser.setUsername(superuserName);
        superUser.setPassword(bCryptPasswordEncoder().encode(superuserPass));
        superUser.setActive(true);
        superUser.setRoles(Arrays.asList(
                authService.getOrCreateRoleIfNotPresent(Role.ADMIN_ROLE),
                authService.getOrCreateRoleIfNotPresent(Role.USER_ROLE)
        ));
        authService.saveUser(superUser);
        return null;
    }
}
