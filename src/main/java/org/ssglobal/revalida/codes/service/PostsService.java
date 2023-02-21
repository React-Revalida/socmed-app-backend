package org.ssglobal.revalida.codes.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.dto.FollowsDTO;
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Posts;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.PostsRepository;

@Service
public class PostsService {

	private final PostsRepository postRepository;

	public PostsService(AppUserRepository appUserRepository, PostsRepository postRepository) {
		this.postRepository = postRepository;
	}

	public Set<PostsDTO> getPostsByUsername(String username) {
		Set<Integer> postIds = postRepository.findAllPostsIdByUsername(username);
		Set<Posts> postsTbl = new HashSet<>();
		System.out.println(postIds);

		for(Integer postId: postIds) {
    		Optional<Posts> userPost = postRepository.findById(postId);
    		if (userPost.isPresent()) {
    			postsTbl.add(userPost.get());
    		}
		}
		return mapToPostsTbl (postsTbl, new HashSet<>());
	}
			
	private Set<PostsDTO> mapToPostsTbl (Set<Posts> postsTbl, Set<PostsDTO> postsDTOTbl) {
		for (Posts post: postsTbl) {
			System.out.println(post.getMessage());
			PostsDTO postsDTO = new PostsDTO();
			postsDTO.setPostId(post.getPostId());
			postsDTO.setMessage(post.getMessage());
			postsDTO.setImageUrl(post.getImageUrl());
			postsDTO.setTimestamp(post.getTimestamp());
			postsDTO.setDeleted(post.getDeleted());
			postsDTO.setUser(post.getUser().getUserId());
			postsDTOTbl.add(postsDTO);
		}
		
		return postsDTOTbl;
	}
}
