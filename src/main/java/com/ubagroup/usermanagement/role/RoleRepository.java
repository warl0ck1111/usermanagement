package com.ubagroup.usermanagement.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Okala III
 */

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String roleName);

    boolean existsByName(String roleName);
}
