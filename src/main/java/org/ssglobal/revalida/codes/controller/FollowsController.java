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
import org.ssglobal.revalida.codes.model.Follows;
import org.ssglobal.revalida.codes.service.FollowsService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowsController {
	
	private final FollowsService followsService;
	
	public FollowsController(FollowsService followsService) {
		this.followsService = followsService;
	}
	
	@GetMapping("/following/{id}")
	public ResponseEntity<Set<Follows>> getUserFollowing(@PathVariable("id") Integer id){
		FollowsDTO followsDTO = new FollowsDTO();
		followsDTO.setFollower(id);
		return new ResponseEntity<>(followsService.getFollowing(followsDTO), null, HttpStatus.SC_OK);
	}
	

}
