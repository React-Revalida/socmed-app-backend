package org.ssglobal.revalida.codes.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Integer> {

    @Query("select l.user from Likes l where l.post.postId = :post") 
	Set<AppUser> findAllByPostId(@Param("post") Integer post);
}
