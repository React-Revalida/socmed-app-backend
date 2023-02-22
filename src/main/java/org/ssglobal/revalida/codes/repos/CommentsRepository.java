package org.ssglobal.revalida.codes.repos;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {
	

    @Query("select l from Comments l where l.post.postId = :post") 
	Set<Comments> findAllByPostId(@Param("post") Integer post);
}
