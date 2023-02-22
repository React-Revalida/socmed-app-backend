package org.ssglobal.revalida.codes.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.dto.FollowsTableDTO;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Follows;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.FollowsRepository;

import jakarta.transaction.Transactional;

@Service
public class FollowsService {

    private static final Logger LOG = LoggerFactory.getLogger(FollowsService.class);
    private final AppUserRepository appUserRepository;
    private final FollowsRepository followsRepository;
    
    public FollowsService(AppUserRepository appUserRepository, FollowsRepository followsRepository) {
    	this.appUserRepository = appUserRepository;
    	this.followsRepository = followsRepository;
    }
    
    public Set<FollowsTableDTO> getFollowing(String username){
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
    
    public Set<FollowsTableDTO> getFollowers(String username){
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
    
    @Transactional
    public Boolean followUser(String username, String usernameToFollow) {
    	AppUser user = appUserRepository.findByUsernameIgnoreCase(username);
    	AppUser userToFollow = appUserRepository.findByUsernameIgnoreCase(usernameToFollow);
    	if (userToFollow != null) {
    		Follows follows = new Follows();
    		mapToFollowsEntity(follows, user, userToFollow);
    		Follows newFollow = followsRepository.save(follows);
    		if (newFollow != null) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    @Transactional 
    public Boolean unfollowUser(String username, String usernameToUnfollow) {
    	AppUser user = appUserRepository.findByUsernameIgnoreCase(username);
    	AppUser userToUnfollow = appUserRepository.findByUsernameIgnoreCase(usernameToUnfollow);
    	Follows follows = followsRepository.findFollowerFollowingPair(user.getUserId(), userToUnfollow.getUserId());
    	if (userToUnfollow != null && follows != null) {
    		followsRepository.delete(follows);
    		return true;
    	}
    	
    	return false;
    }
    
    private Follows mapToFollowsEntity(Follows follows, AppUser user, AppUser userToFollow) {
    	follows.setFollower(user);
    	follows.setFollowing(userToFollow);
    	return follows;
    }

	private Set<FollowsTableDTO> maptoUserFollowingTbl(Set<AppUser> followingTbl, Set<FollowsTableDTO> followingDTOTbl) {
		for (AppUser user: followingTbl) {
			FollowsTableDTO followsDTO = new FollowsTableDTO();
			followsDTO.setUsername(user.getUsername());
			followsDTO.setUserId(user.getUserId());
			followsDTO.setProfilePic(user.getProfile().getProfilePic());
			followingDTOTbl.add(followsDTO);
		}
		
		return followingDTOTbl;
	}

}
