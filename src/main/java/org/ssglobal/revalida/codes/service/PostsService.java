package org.ssglobal.revalida.codes.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.comparator.TimestampComparator;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
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
	private final LikesService likesService;
	private final CommentsService commentsService;

	public PostsService(PostsRepository postRepository, AppUserRepository appUserRepository, ImageService imageService,
			LikesService likesService, CommentsService commentsService) {
		this.postRepository = postRepository;
		this.appUserRepository = appUserRepository;
		this.imageService = imageService;
		this.likesService = likesService;
		this.commentsService = commentsService;

	}

	public Set<PostsDTO> getAllPosts() {
		List<Posts> posts = postRepository.findAll();
		Set<Posts> postsTbl = new HashSet<>();

		for (Posts post : posts) {
			postsTbl.add(post);
		}

		return mapToPostsTbl(postsTbl, new HashSet<>());
	}

	public PostsDTO getPostById(Integer postId) {
		Optional<Posts> confirmedPost = postRepository.findById(postId);
		PostsDTO postDTO = new PostsDTO();
		Posts post = new Posts();

		if (confirmedPost.isPresent()) {
			post = confirmedPost.get();
		}

		return mapToPostEntity(postDTO, post);
	}

	public Set<PostsDTO> getPostsByUsername(String username) {
		Set<Integer> postIds = postRepository.findAllPostsIdByUsername(username);
		Set<Posts> postsTbl = new HashSet<>();

		for (Integer postId : postIds) {
			Optional<Posts> userPost = postRepository.findById(postId);
			if (userPost.isPresent()) {
				postsTbl.add(userPost.get());
			}
		}
		return mapToPostsTbl(postsTbl, new HashSet<>());
	}

	@Transactional
	public Set<PostsDTO> deletePostById(Integer id) {
		Optional<Posts> post = postRepository.findById(id);
		if (post.isPresent()) {
			PostsDTO postDTO = new PostsDTO();
			postDTO.setPostId(post.get().getPostId());
			mapDeletedToPostEntity(postDTO, post.get());
			boolean deleted = postRepository.save(post.get()) != null;
			if (deleted) {
				return getPostsByUsername(post.get().getUser().getUsername());
			}
		}
		return new HashSet<>();
	}

	@Transactional
	public Set<PostsDTO> editPostById(Integer id, final MultipartFile postImage, final PostsDTO editedPost)
			throws IOException {
	
		Optional<Posts> post = postRepository.findById(id);
		// if (postImage != null) {
			imageService.postUpdate(postDir, postImage, post.get());
		// } 

		if (post.isPresent()) {
			mapEditedToPostEntity(editedPost, post.get());
			boolean isEdited = postRepository.save(post.get()) != null;
			if (isEdited) {
				return getPostsByUsername(post.get().getUser().getUsername());
			}
		}
		return new HashSet<>();
	}

	@Transactional
	public Set<PostsDTO> createPost(final PostsDTO postDTO, final MultipartFile postImage, String username)
			throws IOException {
		final Posts post = new Posts();
		if (postImage != null) {
			imageService.postUpload(postDir, postImage, post);
		}

		Optional<AppUser> user = appUserRepository.findByUsername(username);
		if (user.isPresent()) {
			mapCreatedToPostEntity(postDTO, post, user.get());
			Posts newPost = postRepository.save(post);
			if (newPost != null) {
				return getAllPosts();
			}
		}

		return new HashSet<>();
	}

	private PostsDTO mapToPostEntity(PostsDTO postDTO, Posts post) {
		if (post.getDeleted() == false) {
			Optional<AppUser> appUserPost = appUserRepository.findById(post.getUser().getUserId());
			if (appUserPost.isPresent()) {
				post.setUser(appUserPost.get());
				postDTO.setPostId(post.getPostId());
				postDTO.setDeleted(post.getDeleted());
				postDTO.setImageUrl(imageService.getImageUrl(post.getImageUrl()));
				postDTO.setMessage(post.getMessage());
				postDTO.setTimestamp(post.getTimestamp());
				postDTO.setUser(mapToAppUserDTO(
						appUserRepository.findByUsernameIgnoreCase(post.getUser().getUsername()), new AppUserDTO()));
				postDTO.setLikes(likesService.getUsersLikesByPostId(post.getPostId()));
				postDTO.setComments(commentsService.getComments(post.getPostId()));
			}
		}

		return postDTO;
	}

	private Posts mapCreatedToPostEntity(PostsDTO postDTO, Posts post, AppUser user) {
		post.setDeleted(false);
		post.setImageUrl(post.getImageUrl());
		post.setMessage(postDTO.getMessage());
		post.setPostId(postDTO.getPostId());
		post.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		post.setUser(user);
		return post;
	}

	private Posts mapEditedToPostEntity(PostsDTO postDTO, Posts post) {
		post.setMessage(postDTO.getMessage());
		post.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		return post;
	}

	private Posts mapDeletedToPostEntity(PostsDTO postDTO, Posts post) {
		post.setPostId(postDTO.getPostId());
		post.setDeleted(true);
		return post;
	}

	private Set<PostsDTO> mapToPostsTbl(Set<Posts> postsTbl, Set<PostsDTO> postsDTOTbl) {
		for (Posts post : postsTbl) {
			if (post.getDeleted() == false) {
				PostsDTO postsDTO = new PostsDTO();
				Optional<AppUser> appUserPost = appUserRepository.findById(post.getUser().getUserId());
				if (appUserPost.isPresent()) {
					post.setUser(appUserPost.get());
					postsDTO.setPostId(post.getPostId());
					postsDTO.setMessage(post.getMessage());
					postsDTO.setImageUrl(imageService.getImageUrl(post.getImageUrl()));
					postsDTO.setTimestamp(post.getTimestamp());
					postsDTO.setDeleted(post.getDeleted());
					postsDTO.setUser(
							mapToAppUserDTO(appUserRepository.findByUsernameIgnoreCase(post.getUser().getUsername()),
									new AppUserDTO()));
					postsDTO.setLikes(likesService.getUsersLikesByPostId(post.getPostId()));
					postsDTO.setComments(commentsService.getComments(post.getPostId()));
					postsDTOTbl.add(postsDTO);

				}
			}
		}
		return sortByTimestamp(postsDTOTbl);
	}

	private AppUserDTO mapToAppUserDTO(AppUser appUser, AppUserDTO appUserDTO) {
		appUserDTO.setUserId(appUser.getUserId());
		appUserDTO.setFirstname(appUser.getProfile().getFirstname());
		appUserDTO.setMiddlename(appUser.getProfile().getMiddlename());
		appUserDTO.setLastname(appUser.getProfile().getLastname());
		appUserDTO.setUsername(appUser.getUsername());
		appUserDTO.setEmail(appUser.getEmail());
		appUserDTO.setIsActive(appUser.getIsActive());
		appUserDTO.setIsValidated(appUser.getIsValidated());
		appUserDTO.setProfile(appUser.getProfile().getProfileId());
		appUserDTO.setProfilePic(imageService.getImageUrl(appUser.getProfile().getProfilePic()));
		return appUserDTO;
	}

	private Set<PostsDTO> sortByTimestamp(Set<PostsDTO> postsDTOTbl) {
		List<PostsDTO> list = new ArrayList<>(postsDTOTbl);
		List<PostsDTO> sortedlist = list.stream().sorted(new TimestampComparator()).collect(Collectors.toList());
		return new LinkedHashSet<PostsDTO>(sortedlist);
	}
}
