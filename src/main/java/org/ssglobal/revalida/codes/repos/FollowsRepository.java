package org.ssglobal.revalida.codes.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ssglobal.revalida.codes.model.Follows;

public interface FollowsRepository extends JpaRepository<Follows, Integer> {
    @Query("SELECT COUNT(f) FROM Follows f WHERE f.following.username = :username")
    Integer countFollowersByUserUsername(@Param("username") String username);

    @Query("SELECT COUNT(f) FROM Follows f WHERE f.follower.username = :username")
    Integer countFollowingByUserUsername(@Param("username") String username);
}
