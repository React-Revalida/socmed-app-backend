package org.ssglobal.revalida.codes.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.CommentsDTO;
import org.ssglobal.revalida.codes.dto.LikesDTO;
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Comments;
import org.ssglobal.revalida.codes.model.Likes;
import org.ssglobal.revalida.codes.model.Posts;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.FollowsRepository;
import org.ssglobal.revalida.codes.repos.LikesRepository;
import org.ssglobal.revalida.codes.repos.PostsRepository;

@Service
public class LikesService {

	private static final Logger LOG = LoggerFactory.getLogger(LikesService.class);
	private final AppUserRepository appUserRepository;
	private final PostsRepository postsRepository;
	private final LikesRepository likesRepository;
	private final ImageService imageService;

	public LikesService(AppUserRepository appUserRepository, LikesRepository likesRepository,
			PostsRepository postsRepository, ImageService imageService) {
		this.appUserRepository = appUserRepository;
		this.likesRepository = likesRepository;
		this.imageService = imageService;
		this.postsRepository = postsRepository;
	}

	public Set<LikesDTO> getLikesByPostId(Integer postId) {
		Set<AppUser> userIds = likesRepository.findAllByPostId(postId);
//    	Set<AppUser> likesTbl = new HashSet<>();
//    	for (Integer id: userIds) {
//    		Optional<AppUser> user = appUserRepository.findById(id); 
//    		if (user.isPresent()) {
//    			likesTbl.add(user.get());
//    		}
//    	}
		return maptoUserLikesTbl(userIds, new HashSet<>());
	}

	public Set<AppUserDTO> getUsersLikesByPostId(Integer postId) {
		Set<AppUser> userIds = likesRepository.findAllByPostId(postId);
		Set<AppUserDTO> likesTbl = new HashSet<>();
		for (AppUser id : userIds) {
			AppUserDTO userDTO = new AppUserDTO();
			userDTO.setUsername(id.getUsername());
			userDTO.setUserId(id.getUserId());
			likesTbl.add(userDTO);
		}
		return likesTbl;
	}

	@Transactional
	public Set<PostsDTO> getPostsLikedByUserId(Integer userId) {
		Set<Posts> posts = likesRepository.findAllLikedPostByUserId(userId);
		Set<PostsDTO> postsTbl = new HashSet<>();
		for (Posts id : posts) {
			PostsDTO postDTO = new PostsDTO();
			postDTO.setPostId(id.getPostId());
			postDTO.setDeleted(id.getDeleted());
			postDTO.setImageUrl(imageService.getImageUrl(id.getImageUrl()));
			postDTO.setTimestamp(id.getTimestamp());
			postDTO.setMessage(id.getMessage());
			postDTO.setUser(maptoUserPostTbl(id.getUser()));
			postDTO.setComments(maptoCommentsPostTbl(id.getComments(), new HashSet<>()));
			postDTO.setMessage(id.getMessage());
			postDTO.setLikes(getUsersLikesByPostId(id.getPostId()));
			postsTbl.add(postDTO);
		}
		return postsTbl;
	}

	private AppUserDTO maptoUserPostTbl(AppUser user) {
		AppUserDTO userDTO = new AppUserDTO();
		userDTO.setUserId(user.getUserId());
		userDTO.setUsername(user.getUsername());
		userDTO.setFirstname(user.getProfile().getFirstname());
		userDTO.setLastname(user.getProfile().getLastname());
		userDTO.setProfilePic(user.getProfile().getProfilePic());

		return userDTO;
	}

	private Set<CommentsDTO> maptoCommentsPostTbl(Set<Comments> comments, Set<CommentsDTO> commentsDTOTbl) {
		for (Comments comment : comments) {
			CommentsDTO commentDTO = new CommentsDTO();
			commentDTO.setId(comment.getId());
			commentsDTOTbl.add(commentDTO);
		}

		return commentsDTOTbl;
	}

	private Set<LikesDTO> maptoUserLikesTbl(Set<AppUser> likesTbl, Set<LikesDTO> likesDTOTbl) {
		for (AppUser user : likesTbl) {
			LikesDTO likesDTO = new LikesDTO();
			likesDTO.setUser(user.getUserId());
			likesDTO.setLiked(true);
			;
			likesDTOTbl.add(likesDTO);
		}

		return likesDTOTbl;
	}

	public Boolean likePost(final LikesDTO likesDTO) throws IOException {
		final Likes like = new Likes();

		Optional<Posts> post = postsRepository.findById(likesDTO.getPost());
		Optional<AppUser> user = appUserRepository.findById(likesDTO.getUser());
		if (user.isPresent()) {
			mapPostUserToLikesTbl(like, likesDTO, post.get(), user.get());
			Likes newLike = likesRepository.save(like);
//			likesRepository.deleteById(null);
			if (newLike != null) {
				return true;
			}
		}

		return false;
	}

	@Transactional
	public Boolean unlikePost(Integer postId, Integer userId) throws IOException {
		likesRepository.deleteLikeRecord(postId, userId);
		return true;
	}

	private Likes mapPostUserToLikesTbl(Likes likes, LikesDTO likesDTO, Posts post, AppUser user) {
		likes.setId(likesDTO.getId());
		likes.setLiked(true);
		likes.setPost(post);
		likes.setUser(user);

		return likes;
	}

}
