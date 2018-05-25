package org.projectzero.sample.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class SampleResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleResourceApplication.class, args);
    }

}
