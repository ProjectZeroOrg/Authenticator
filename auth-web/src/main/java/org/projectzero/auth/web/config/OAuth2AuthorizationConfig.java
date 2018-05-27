package org.projectzero.auth.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.client.clientId:Challenge Me}")
    private String authClientId;

    @Value("${security.oauth2.client.clientSecret:challengeMeSecret}")
    private String authClientSecret;

    @Value("${security.oauth2.client.authorized-grant-types:authorization_code,refresh_token,client_credentials}")
    private String authGrantTypes;

    @Value("${security.oauth2.client.scope:openid, profile}")
    private String authClientScope;

    @Value("${security.oauth2.client.auto-approve:false}")
    private boolean autoApprove;

    @Value("${security.oauth2.resource.jwt.private-key}")
    private String jwtPrivateKey;

    @Value("${security.oauth2.resource.jwt.public-key}")
    private String jwtPublicKey;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(authClientId)
                .secret(authClientSecret)
                .authorizedGrantTypes(authGrantTypes.split(","))
                .scopes(authClientScope.split(","))
                .accessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(1))
                .refreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(2))
                .autoApprove(autoApprove)
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
        ;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtPrivateKey);
        converter.setVerifierKey(jwtPublicKey);
        return converter;
    }
}
