package org.ssglobal.revalida.codes.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssglobal.revalida.codes.model.Posts;

public interface PostsRepository extends JpaRepository<Posts, Integer> {
	
//  Bug: ConverterNotFoundException 
//	Workaround: Used findAll method of JpaRepo to fetch data
	
//	@Query("select p.postId, p.message, p.imageUrl, p.timestamp, p.deleted, p.user from Posts p where p.user.username = :username")
//	Set<Posts> findAllPostsByUsername(@Param("username") String username);
		
	
}
