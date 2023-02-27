package org.ssglobal.revalida.codes.service;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
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
	private final ImageService imageService;
    
    public FollowsService(AppUserRepository appUserRepository, FollowsRepository followsRepository, ImageService imageService) {
    	this.appUserRepository = appUserRepository;
    	this.followsRepository = followsRepository;
		this.imageService = imageService;
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

	public Set<AppUserDTO> getMutualFollows(String username) {
		AppUser user = appUserRepository.findByUsernameIgnoreCase(username);
		return followsRepository.findMutualFollows(user).stream().map(follows -> {
			return mapAppUserToFollow(follows, new AppUserDTO());
		}).collect(Collectors.toSet());
	}

	private AppUserDTO mapAppUserToFollow(Follows following, AppUserDTO appUserDTO) {
		appUserDTO.setUserId(following.getFollowing().getUserId());
        appUserDTO.setFirstname(following.getFollowing().getProfile().getFirstname());
        appUserDTO.setMiddlename(following.getFollowing().getProfile().getMiddlename());
        appUserDTO.setLastname(following.getFollowing().getProfile().getLastname());
        appUserDTO.setPhone(following.getFollowing().getProfile().getPhone());
        appUserDTO.setBirthdate(following.getFollowing().getProfile().getBirthdate());
        appUserDTO.setUsername(following.getFollowing().getUsername());
        appUserDTO.setEmail(following.getFollowing().getEmail());
        appUserDTO.setIsActive(following.getFollowing().getIsActive());
        appUserDTO.setIsValidated(following.getFollowing().getIsValidated());
        appUserDTO.setProfile(following.getFollowing().getProfile().getProfileId());
        String name = following.getFollowing().getProfile().getMiddlename() != null
                ? String.format("%s %s %s", following.getFollowing().getProfile().getFirstname(), following.getFollowing().getProfile().getMiddlename(),
				following.getFollowing().getProfile().getLastname())
                : String.format("%s %s", following.getFollowing().getProfile().getFirstname(), following.getFollowing().getProfile().getLastname());
        appUserDTO.setName(name);
        appUserDTO.setBio(following.getFollowing().getProfile().getDescription());
        appUserDTO.setGender(following.getFollowing().getProfile().getGender().toString());
        appUserDTO.setProfilePic(imageService.getImageUrl(following.getFollowing().getProfile().getProfilePic()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String dateJoined = following.getFollowing().getDateCreated().format(formatter);
        appUserDTO.setDateJoined(dateJoined);
        final Integer followersOfMutual = followsRepository.countFollowersByUserUsername(following.getFollowing().getUsername());
        final Integer followingOfMutual = followsRepository.countFollowingByUserUsername(following.getFollowing().getUsername());
        appUserDTO.setFollowers(followersOfMutual);
        appUserDTO.setFollowing(followingOfMutual);
        return appUserDTO;
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
			followsDTO.setName(String.join(" ", user.getProfile().getFirstname(), user.getProfile().getLastname()));
			followsDTO.setProfilePic(imageService.getImageUrl(user.getProfile().getProfilePic()));
			followingDTOTbl.add(followsDTO);
		}
		
		return followingDTOTbl;
	}

}
