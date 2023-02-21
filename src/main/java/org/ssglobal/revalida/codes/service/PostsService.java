package org.ssglobal.revalida.codes.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.dto.LikesDTO;
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Posts;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.PostsRepository;

@Service
public class PostsService {

	private final AppUserRepository appUserRepository;
	private final PostsRepository postRepository;

	public PostsService(AppUserRepository appUserRepository, PostsRepository postRepository) {
		this.appUserRepository = appUserRepository;
		this.postRepository = postRepository;
	}

	public Set<PostsDTO> getPostsByUsername(String username) {
		Optional<AppUser> user = appUserRepository.findByUsername(username);
		Set<PostsDTO> postDTOTbl = new HashSet<>();

		if (user.isPresent()) {
			AppUser userConfirmed = appUserRepository.findByUsernameIgnoreCase(username);
			List<Posts> userPosts = postRepository.findAll();
			for (Posts post : userPosts) {
				if (userConfirmed.getUserId().equals(post.getUser().getUserId())) {
					PostsDTO postDTO = new PostsDTO();
					postDTO.setPostId(post.getPostId());
					postDTO.setMessage(post.getMessage());
					postDTO.setImageUrl(post.getImageUrl());
					postDTO.setTimestamp(post.getTimestamp());
					postDTO.setDeleted(post.getDeleted());
					postDTO.setUser(post.getUser().getUserId());
					postDTOTbl.add(postDTO);
				};
			};
		};
		return postDTOTbl;
	};
}
