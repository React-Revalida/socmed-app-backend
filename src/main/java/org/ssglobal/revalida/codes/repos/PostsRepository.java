package org.ssglobal.revalida.codes.repos;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ssglobal.revalida.codes.model.Posts;

public interface PostsRepository extends JpaRepository<Posts, Integer> {
	
	@Query("select p.postId, p.message, p.imageUrl, p.timestamp, p.deleted, p.user from Posts p where p.user.username = :username")
	Set<Posts> findAllPostsByUsername(@Param("username") String username);
		
	
}
