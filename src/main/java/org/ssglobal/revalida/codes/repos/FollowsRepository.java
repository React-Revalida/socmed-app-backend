package org.ssglobal.revalida.codes.repos;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ssglobal.revalida.codes.model.Follows;

public interface FollowsRepository extends JpaRepository<Follows, Integer> {
	
	@Query("select f from Follows f where f.follower.userId = :follower")
	Set<Follows> findByFollower(@Param("follower") Integer follower);
	
}
