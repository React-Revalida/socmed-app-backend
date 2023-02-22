package org.ssglobal.revalida.codes.controller;

import java.util.Set;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.CommentsDTO;
import org.ssglobal.revalida.codes.service.CommentsService;

@RestController
@RequestMapping(value = "/api/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentsController {
	
	private final CommentsService commentsService;

	public CommentsController(CommentsService commentsService) {
		this.commentsService = commentsService;
	}

	
	@GetMapping("/post/{post}")
	public ResponseEntity<Set<CommentsDTO>> getUsersLikes(@PathVariable Integer post){
		Set<CommentsDTO> likesDTOTbl = commentsService.getComments(post);
		return new ResponseEntity<>(likesDTOTbl, null, HttpStatus.SC_OK);
	}
}
