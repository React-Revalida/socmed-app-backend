package org.ssglobal.revalida.codes.repos;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ssglobal.revalida.codes.model.Posts;

public interface PostsRepository extends JpaRepository<Posts, Integer> {

	@Query("select p.postId from Posts p where p.user.username = :username and p.deleted <> TRUE")
	Set<Integer> findAllPostsIdByUsername(@Param("username") String username);

}
