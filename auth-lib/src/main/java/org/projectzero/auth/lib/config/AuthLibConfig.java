package org.projectzero.auth.lib.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "org.projectzero.auth.lib.service")
@EnableJpaRepositories("org.projectzero.auth.lib.repository")
@EntityScan("org.projectzero.auth.lib.entity")
public class AuthLibConfig {
}
