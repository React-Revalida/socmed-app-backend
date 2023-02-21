package org.ssglobal.revalida.codes.controller;

import java.util.Set;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.service.PostsService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostsController {

	private final PostsService postsService;

	public PostsController(PostsService postsService) {
		this.postsService = postsService;
	}

	@GetMapping("/profile/{username}/posts")
	public ResponseEntity<Set<PostsDTO>> getUserPosts(@PathVariable String username) {
		Set<PostsDTO> postsDTOTbl = postsService.getPostsByUsername(username);
		return new ResponseEntity<>(postsDTOTbl, null, HttpStatus.SC_OK);

	}
	
	@PostMapping("/posts/delete/{id}")
	public ResponseEntity<Boolean> deletePost(@PathVariable Integer id) {
		Boolean deleted = postsService.deletePostById(id);
		return new ResponseEntity<>(deleted, null, HttpStatus.SC_OK);
	}

}
