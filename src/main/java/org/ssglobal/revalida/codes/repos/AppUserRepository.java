package org.ssglobal.revalida.codes.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssglobal.revalida.codes.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByUsernameOrEmail(String username, String email);

    AppUser findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

}
