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
import org.ssglobal.revalida.codes.dto.LikesDTO;
import org.ssglobal.revalida.codes.service.LikesService;


@RestController
@RequestMapping(value = "/api/likes", produces = MediaType.APPLICATION_JSON_VALUE)
public class LikesController {

	private final LikesService likesService;

	public LikesController(LikesService likesService) {
		this.likesService = likesService;
	}
	
	@GetMapping("/{post}")
	public ResponseEntity<Set<LikesDTO>> getUserFollowing(@PathVariable Integer post){
		Set<LikesDTO> likesDTOTbl = likesService.getLikesByPostId(post);
//        return new ResponseEntity<>(appUserDTO, HttpStatus.OK);
//		return new ResponseEntity<>(likesDTOTbl, HttpStatus.SC_OK);
		return new ResponseEntity<>(likesDTOTbl, null, HttpStatus.SC_OK);
	}
	
}
