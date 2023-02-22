package org.ssglobal.revalida.codes.controller;

import java.io.IOException;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.FollowsDTO;
import org.ssglobal.revalida.codes.dto.LikesDTO;
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.model.AppUser;
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
	
	@PostMapping("/post")
	public ResponseEntity<Boolean> likePost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token, 
			@RequestPart("like") @Valid final LikesDTO likesDTO) throws IOException {
		Boolean liked = likesService.likePost(likesDTO);
		return new ResponseEntity<>(liked, null, HttpStatus.SC_OK);
	}
	
	@PostMapping("/unlike/{likeId}")
	public ResponseEntity<Boolean> unlikePost(@PathVariable Integer likeId) throws IOException {
		Boolean unliked = likesService.unlikePost(likeId);
		return new ResponseEntity<>(unliked, null, HttpStatus.SC_OK);
	}
	
}
