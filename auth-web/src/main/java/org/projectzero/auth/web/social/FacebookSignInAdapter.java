package org.projectzero.auth.web.social;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.projectzero.auth.lib.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
public class FacebookSignInAdapter implements SignInAdapter {

    private static final Log logger = LogFactory.getLog(FacebookSignInAdapter.class);

    private String defaultRedirectUri;

    public void setDefaultRedirectUri(String defaultRedirectUri) {
        this.defaultRedirectUri = defaultRedirectUri;
    }

    @Override
    public String signIn(String s, Connection<?> connection, NativeWebRequest nativeWebRequest) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        connection.getDisplayName(),
                        null,
                        Arrays.asList(new SimpleGrantedAuthority(Role.FACEBOOK_ROLE))
                )
        );

        try {
            String queryString = ((DefaultSavedRequest) nativeWebRequest.getAttribute("SPRING_SECURITY_SAVED_REQUEST", 1)).getQueryString();
            return UriComponentsBuilder.fromUriString("?" + queryString).build().getQueryParams().get("redirect_uri").get(0);
        } catch (Exception e) {
            logger.error("Failed to get original redirect uri. Switching to default.", e);
            return defaultRedirectUri;
        }
    }
}
