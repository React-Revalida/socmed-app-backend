package org.ssglobal.revalida.codes.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.CommentsDTO;
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.service.CommentsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentsController {
	
	private final CommentsService commentsService;

	@Autowired
	private JwtDecoder jwtDecoder;
	
	public CommentsController(CommentsService commentsService) {
		this.commentsService = commentsService;
	}

	
	@GetMapping("/comments/post/{post}")
	public ResponseEntity<Set<CommentsDTO>> getUsersLikes(@PathVariable Integer post){
		Set<CommentsDTO> likesDTOTbl = commentsService.getComments(post);
		return new ResponseEntity<>(likesDTOTbl, null, HttpStatus.SC_OK);
	}
	
	@PostMapping("/comments")
	public ResponseEntity<Boolean> addComment(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
			@RequestPart("comments") @Valid final CommentsDTO commentsDTO)
			throws IOException {
//		String jwtToken = token.replace("Bearer ", "");
//		Jwt jwt = jwtDecoder.decode(jwtToken);
//		Integer user = jwt.getClaim("userId");
		Boolean added = commentsService.createComment(commentsDTO);
		return new ResponseEntity<>(added, null, HttpStatus.SC_OK);
	}
}
