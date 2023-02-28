package org.ssglobal.revalida.codes.controller;

import java.io.IOException;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.LikesDTO;
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.service.LikesService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/likes", produces = MediaType.APPLICATION_JSON_VALUE)
public class LikesController {

	private final LikesService likesService;

	public LikesController(LikesService likesService) {
		this.likesService = likesService;
	}
	
	@GetMapping("/post/{post}")
	public ResponseEntity<Set<AppUserDTO>> getUsersLikes(@PathVariable Integer post){
		Set<AppUserDTO> likesDTOTbl = likesService.getUsersLikesByPostId(post);
		return new ResponseEntity<>(likesDTOTbl, null, HttpStatus.SC_OK);
	}
	 
	@GetMapping("/likedpost/{user}")
	public ResponseEntity<Set<PostsDTO>> getLikedPost(@PathVariable Integer user){
		Set<PostsDTO> likesDTOTbl = likesService.getPostsLikedByUserId(user);
		return new ResponseEntity<>(likesDTOTbl, null, HttpStatus.SC_OK);
	}
	
	
	@PostMapping("/post")
	public ResponseEntity<Boolean> likePost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token, 
			@RequestPart("like") @Valid final LikesDTO likesDTO) throws IOException {
		Boolean liked = likesService.likePost(likesDTO);
		return new ResponseEntity<>(liked, null, HttpStatus.SC_OK);
	}
	
	@PostMapping("/unlike/{postId}/{userId}")
	public ResponseEntity<Boolean> unlikePost(@PathVariable Integer postId, @PathVariable Integer userId) throws IOException {
		Boolean unliked = likesService.unlikePost(postId, userId);
		return new ResponseEntity<>(unliked, null, HttpStatus.SC_OK);
	}
	
}
