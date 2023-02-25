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
import org.ssglobal.revalida.codes.dto.PostsDTO;
import org.ssglobal.revalida.codes.model.Posts;
import org.ssglobal.revalida.codes.service.PostsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostsController {

	private final PostsService postsService;

	@Autowired
	private JwtDecoder jwtDecoder;

	public PostsController(PostsService postsService) {
		this.postsService = postsService;
	}
	
	@GetMapping("/posts")
	public ResponseEntity<Set<PostsDTO>> getAllPosts() {
		Set<PostsDTO> postsDTOTbl = postsService.getAllPosts();
		return new ResponseEntity<>(postsDTOTbl, null, HttpStatus.SC_OK);
	}

	@GetMapping("/profile/{username}/posts")
	public ResponseEntity<Set<PostsDTO>> getUserPosts(@PathVariable String username) {
		Set<PostsDTO> postsDTOTbl = postsService.getPostsByUsername(username);
		return new ResponseEntity<>(postsDTOTbl, null, HttpStatus.SC_OK);
	}

	@GetMapping("/post/{postId}")
	public ResponseEntity<PostsDTO> getPostById(@PathVariable Integer postId) {
		PostsDTO post = postsService.getPostById(postId);
		return new ResponseEntity<>(post, null, HttpStatus.SC_OK);
	}


	@PostMapping("/posts/delete/{id}")
	public ResponseEntity<Boolean> deletePost(@PathVariable Integer id) {
		Boolean deleted = postsService.deletePostById(id);
		return new ResponseEntity<>(deleted, null, HttpStatus.SC_OK);
	}

	@PostMapping("/posts")
	public Set<PostsDTO> addPost(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
			@RequestPart("post") @Valid final PostsDTO postDTO, @RequestPart("image") MultipartFile postImg)
			throws IOException {
		String jwtToken = token.replace("Bearer ", "");
		Jwt jwt = jwtDecoder.decode(jwtToken);
		String user = jwt.getClaim("user");
//		Boolean deleted = postsService.createPost(postDTO, postImg, user);
//		return new ResponseEntity<>(deleted, null, HttpStatus.SC_OK);
		return postsService.createPost(postDTO, postImg, user);
	}

}
