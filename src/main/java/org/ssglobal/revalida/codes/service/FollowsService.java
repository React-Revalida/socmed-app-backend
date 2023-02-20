package org.ssglobal.revalida.codes.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.dto.FollowsDTO;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.FollowsRepository;

@Service
public class FollowsService {

    private static final Logger LOG = LoggerFactory.getLogger(FollowsService.class);
    private final AppUserRepository appUserRepository;
    private final FollowsRepository followsRepository;
    
    public FollowsService(AppUserRepository appUserRepository, FollowsRepository followsRepository) {
    	this.appUserRepository = appUserRepository;
    	this.followsRepository = followsRepository;
    }
    
    public Set<FollowsDTO> getFollowing(String username){
    	Set<Integer> ids = followsRepository.findFollowingByUsername(username);
    	Set<AppUser> followingTbl = new HashSet<>();
    	for (Integer id: ids) {
    		Optional<AppUser> user = appUserRepository.findById(id); 
    		if (user.isPresent()) {
    			followingTbl.add(user.get());
    		}
    	}
    	
    	return maptoUserFollowingTbl(followingTbl, new HashSet<>());
    }
    
    public Set<FollowsDTO> getFollowers(String username){
    	Set<Integer> ids = followsRepository.findFollowersByUsername(username);
    	Set<AppUser> followingTbl = new HashSet<>();
    	for (Integer id: ids) {
    		Optional<AppUser> user = appUserRepository.findById(id); 
    		if (user.isPresent()) {
    			followingTbl.add(user.get());
    		}
    	}
    	
    	return maptoUserFollowingTbl(followingTbl, new HashSet<>());
    }

	private Set<FollowsDTO> maptoUserFollowingTbl(Set<AppUser> followingTbl, Set<FollowsDTO> followingDTOTbl) {
		for (AppUser user: followingTbl) {
			FollowsDTO followsDTO = new FollowsDTO();
			followsDTO.setUsername(user.getUsername());
			followsDTO.setUserId(user.getUserId());
			followsDTO.setProfilePic(user.getProfile().getProfilePic());
			followingDTOTbl.add(followsDTO);
		}
		
		return followingDTOTbl;
	}

}
