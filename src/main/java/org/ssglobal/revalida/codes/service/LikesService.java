package org.ssglobal.revalida.codes.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.LikesDTO;
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.model.AppUser;
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
    
	public LikesService(AppUserRepository appUserRepository, LikesRepository likesRepository, PostsRepository postsRepository) {
		this.appUserRepository = appUserRepository;
		this.likesRepository = likesRepository;
		this.postsRepository = postsRepository;
	}
    
	public Set<LikesDTO> getLikesByPostId(Integer postId){
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
	
	public Set<AppUserDTO> getUsersLikesByPostId(Integer postId){
		Set<AppUser> userIds = likesRepository.findAllByPostId(postId);
    	Set<AppUserDTO> likesTbl = new HashSet<>();
    	for (AppUser id: userIds) {
    		AppUserDTO userDTO = new AppUserDTO();
    		userDTO.setUsername(id.getUsername());
    		userDTO.setUserId(id.getUserId());
    		likesTbl.add(userDTO);
    	}
		return likesTbl;
	}
    
	
	private Set<LikesDTO> maptoUserLikesTbl(Set<AppUser> likesTbl, Set<LikesDTO> likesDTOTbl) {
		for (AppUser user: likesTbl) {
			LikesDTO likesDTO = new LikesDTO();
			likesDTO.setUser(user.getUserId());
			likesDTO.setLiked(true);;
			likesDTOTbl.add(likesDTO);
		}
		
		return likesDTOTbl;
	}
	
	public Boolean likePost(final LikesDTO likesDTO) 
			throws IOException {
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
	
	public Boolean unlikePost(Integer likesId) 
			throws IOException {
		likesRepository.deleteById(likesId);
		return true;
	}
	

	private Likes mapPostUserToLikesTbl (Likes likes, LikesDTO likesDTO, Posts post, AppUser user) {
			likes.setId(likesDTO.getId());
			likes.setLiked(true);
			likes.setPost(post);
			likes.setUser(user);
		
		return likes;
	}
	
	
	
}
