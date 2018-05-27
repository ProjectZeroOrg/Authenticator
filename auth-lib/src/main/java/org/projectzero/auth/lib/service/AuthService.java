package org.projectzero.auth.lib.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.projectzero.auth.lib.entity.Role;
import org.projectzero.auth.lib.entity.User;
import org.projectzero.auth.lib.repository.RoleRepository;
import org.projectzero.auth.lib.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {

    private static final Log logger = LogFactory.getLog(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    @Transactional
    public Role getOrCreateRoleIfNotPresent(final String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(roleName);
                    return roleRepository.save(r);
                });
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(AuthService::getUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Login incorrect"));
    }

    private static org.springframework.security.core.userdetails.User getUserDetails(final User user) {
        if (user.isActive()) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    getAuthorities(user)
            );
        } else {
            throw new DisabledException("Inactive account");
        }
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(final User user) {
        try {
            return user.getRoles().stream()
                    .map(r -> new SimpleGrantedAuthority(r.getName()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.warn("Failed to get user roles. Assigning a default user role.", e);
            return Arrays.asList(new SimpleGrantedAuthority(Role.USER_ROLE));
        }
    }
}
