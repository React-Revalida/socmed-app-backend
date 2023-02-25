package org.ssglobal.revalida.codes.controller;

import java.util.Set;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.FollowsTableDTO;
import org.ssglobal.revalida.codes.service.FollowsService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowsController {
	
	private final FollowsService followsService;
	
	@Autowired
	private JwtDecoder jwtDecoder;
	
	public FollowsController(FollowsService followsService) {
		this.followsService = followsService;
	}
	
	@GetMapping("/following/{username}")
	public ResponseEntity<Set<FollowsTableDTO>> getUserFollowing(@PathVariable String username){
		Set<FollowsTableDTO> followsDTOTbl = followsService.getFollowing(username);
		return new ResponseEntity<>(followsDTOTbl, null, HttpStatus.SC_OK);
	}
	
	@GetMapping("/followers/{username}")
	public ResponseEntity<Set<FollowsTableDTO>> getUserFollowers(@PathVariable String username){
		Set<FollowsTableDTO> followsDTOTbl = followsService.getFollowers(username);
		return new ResponseEntity<>(followsDTOTbl, null, HttpStatus.SC_OK);
	}
	
	@PostMapping("/follow/{username}")
	public ResponseEntity<Boolean> followUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token, 
			@PathVariable String username) {
		String jwtToken = token.replace("Bearer ", "");
		Jwt jwt = jwtDecoder.decode(jwtToken);
		String user = jwt.getClaim("user");
		Boolean followed = followsService.followUser(user, username);
		return new ResponseEntity<>(followed, null, HttpStatus.SC_OK);
	}
	
	@PostMapping("unfollow/{username}")
	public ResponseEntity<Boolean> unfollowUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token, 
			@PathVariable String username) {
		String jwtToken = token.replace("Bearer ", "");
		Jwt jwt = jwtDecoder.decode(jwtToken);
		String user = jwt.getClaim("user");
		Boolean unfollowed = followsService.unfollowUser(user, username);
		return new ResponseEntity<>(unfollowed, null, HttpStatus.SC_OK);
	}

	@PostMapping("/mutually-followed")
	public ResponseEntity<Set<AppUserDTO>> mutualFollow(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
		String jwtToken = token.replace("Bearer ", "");
		Jwt jwt = jwtDecoder.decode(jwtToken);
		String user = jwt.getClaim("user");
		Set<AppUserDTO> mutual = followsService.getMutualFollows(user);
		return new ResponseEntity<>(mutual, null, HttpStatus.SC_OK);
	}
}
