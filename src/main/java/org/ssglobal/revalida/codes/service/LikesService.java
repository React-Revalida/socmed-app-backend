package org.ssglobal.revalida.codes.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.dto.LikesDTO;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.FollowsRepository;
import org.ssglobal.revalida.codes.repos.LikesRepository;

@Service
public class LikesService {

    private static final Logger LOG = LoggerFactory.getLogger(LikesService.class);
    private final AppUserRepository appUserRepository;
    private final LikesRepository likesRepository;
    
	public LikesService(AppUserRepository appUserRepository, LikesRepository likesRepository) {
		this.appUserRepository = appUserRepository;
		this.likesRepository = likesRepository;
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
    
	
	private Set<LikesDTO> maptoUserLikesTbl(Set<AppUser> likesTbl, Set<LikesDTO> likesDTOTbl) {
		for (AppUser user: likesTbl) {
			LikesDTO likesDTO = new LikesDTO();
			likesDTO.setUser(user.getUserId());
			likesDTO.setLiked(true);;
			likesDTOTbl.add(likesDTO);
		}
		
		return likesDTOTbl;
	}
}
