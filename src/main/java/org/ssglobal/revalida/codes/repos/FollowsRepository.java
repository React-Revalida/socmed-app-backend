package org.ssglobal.revalida.codes.repos;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Follows;

public interface FollowsRepository extends JpaRepository<Follows, Integer> {
    @Query("SELECT COUNT(f) FROM Follows f WHERE f.following.username = :username")
    Integer countFollowersByUserUsername(@Param("username") String username);

    @Query("SELECT COUNT(f) FROM Follows f WHERE f.follower.username = :username")
    Integer countFollowingByUserUsername(@Param("username") String username);
    
    @Query("select f.following.userId from Follows f where f.follower.username = :username")
    Set<Integer> findFollowingByUsername(@Param("username") String username);
    
    @Query("select f.follower.userId from Follows f where f.following.username = :username")
    Set<Integer> findFollowersByUsername(@Param("username") String username);
    
    @Query("select f from Follows f where f.follower.userId = :follower_id and f.following.userId = :following_id")
    Follows findFollowerFollowingPair(@Param("follower_id") Integer followerId, @Param("following_id") Integer following_id);

    @Query("SELECT f FROM Follows f WHERE f.follower = :user AND f.following IN (SELECT f2.follower FROM Follows f2 WHERE f2.following = :user)")
    Set<Follows> findMutualFollows(@Param("user") AppUser user);
}
