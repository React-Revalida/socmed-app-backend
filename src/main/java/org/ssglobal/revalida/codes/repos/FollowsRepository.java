package org.ssglobal.revalida.codes.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssglobal.revalida.codes.model.Follows;

public interface FollowsRepository extends JpaRepository<Follows, Integer> {
}
