package org.ssglobal.revalida.codes.controller;

import java.util.Set;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.revalida.codes.dto.FollowsDTO;
import org.ssglobal.revalida.codes.service.FollowsService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowsController {
	
	private final FollowsService followsService;
	
	public FollowsController(FollowsService followsService) {
		this.followsService = followsService;
	}
	
	@GetMapping("/following/{username}")
	public ResponseEntity<Set<FollowsDTO>> getUserFollowing(@PathVariable String username){
		Set<FollowsDTO> followsDTOTbl = followsService.getFollowing(username);
		return new ResponseEntity<>(followsDTOTbl, null, HttpStatus.SC_OK);
	}
	
	@GetMapping("/followers/{username}")
	public ResponseEntity<Set<FollowsDTO>> getUserFollowers(@PathVariable String username){
		Set<FollowsDTO> followsDTOTbl = followsService.getFollowers(username);
		return new ResponseEntity<>(followsDTOTbl, null, HttpStatus.SC_OK);
	}

}
