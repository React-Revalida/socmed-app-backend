package org.ssglobal.revalida.codes.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Likes;
import org.ssglobal.revalida.codes.model.Posts;

public interface LikesRepository extends JpaRepository<Likes, Integer> {

    @Query("select l.user from Likes l where l.post.postId = :post AND l.liked=true") 
	Set<AppUser> findAllByPostId(@Param("post") Integer post);
    
    @Query("select l.post from Likes l where l.user.userId = :user AND l.liked=true") 
    Set<Posts> findAllLikedPostByUserId(@Param("user") Integer user);
    
    @Modifying
    @Query("delete from Likes l where l.post.postId = :post AND l.user.userId= :user") 
	void deleteLikeRecord(@Param("post") Integer post, @Param("user")Integer user);
    
    
    
}
