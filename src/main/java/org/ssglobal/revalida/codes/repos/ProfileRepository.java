package org.ssglobal.revalida.codes.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssglobal.revalida.codes.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Profile findByProfile_Username(String username);
}
