package org.projectzero.auth.web;

import org.projectzero.auth.lib.config.AuthLibConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AuthLibConfig.class)
public class AuthWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthWebApplication.class, args);
    }
}
