package org.projectzero.auth.lib.repository;

import org.projectzero.auth.lib.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(final String roleName);
}
