package org.ssglobal.revalida.codes.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Posts;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.PostsRepository;

import jakarta.transaction.Transactional;

@Service
public class PostsService {
	
    @Value("${do.space.post-dir}")
    private String postDir;

	private final PostsRepository postRepository;
	private final AppUserRepository appUserRepository;
	private final ImageService imageService;

	public PostsService(PostsRepository postRepository, AppUserRepository appUserRepository, ImageService imageService) {
		this.postRepository = postRepository;
		this.appUserRepository = appUserRepository;
		this.imageService = imageService;
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
	
	@Transactional
	public Boolean deletePostById(Integer id) {
		Optional<Posts> post = postRepository.findById(id);
		if (post.isPresent()) {
			PostsDTO postDTO = new PostsDTO();
			postDTO.setPostId(post.get().getPostId());
			mapDeletedToPostEntity(postDTO, post.get());
	        boolean deleted = postRepository.save(post.get()) != null;
	        return deleted;
		}
		
		return false;
	}
	
	@Transactional
	public Boolean createPost(final PostsDTO postDTO, final MultipartFile postImage, String username) 
			throws IOException {
		final Posts post = new Posts();
		if (postImage != null) {
			imageService.postUpload(postDir, postImage, post);
		}
		
		Optional<AppUser> user = appUserRepository.findByUsername(username);
		if (user.isPresent()) {
			mapToPostEntity(postDTO, post, user.get());
			Posts newPost = postRepository.save(post);
			if (newPost != null) {
				return true;
			}
		}
		
		return false;
	}
	
	private Posts mapToPostEntity(PostsDTO postDTO, Posts post, AppUser user) {
		post.setDeleted(false);
		post.setImageUrl(post.getImageUrl());
		post.setMessage(postDTO.getMessage());
		post.setPostId(postDTO.getPostId());
		post.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		post.setUser(user);
		return post;
	}
	
	private Posts mapDeletedToPostEntity(PostsDTO postDTO, Posts post) {
		post.setPostId(postDTO.getPostId());
		post.setDeleted(true);
		return post;
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
