package org.ssglobal.revalida.codes.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.dto.FollowsDTO;
import org.ssglobal.revalida.codes.model.Follows;
import org.ssglobal.revalida.codes.repos.FollowsRepository;

@Service
public class FollowsService {

    private static final Logger LOG = LoggerFactory.getLogger(FollowsService.class);
    private final FollowsRepository followsRepository;
    
    public FollowsService(FollowsRepository followsRepository) {
    	this.followsRepository = followsRepository;
    }
    
    public Set<Follows> getFollowing(FollowsDTO followsDTO){
    	return followsRepository.findByFollower(followsDTO.getFollower());
    }

}
