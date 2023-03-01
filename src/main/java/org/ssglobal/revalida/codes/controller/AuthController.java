package org.ssglobal.revalida.codes.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.revalida.codes.dto.records.AuthenticationDTO;
import org.ssglobal.revalida.codes.dto.records.TokenDTO;
import org.ssglobal.revalida.codes.service.auth.AuthService;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> login(@RequestBody AuthenticationDTO user) {
        TokenDTO token = authService.login(user.username(), user.password());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token.accessToken())
                .body(token);
    }
    
    @GetMapping("/forgot-password")
    public ResponseEntity<TokenDTO> forgotPassword(@RequestParam String email) throws Exception {
    	TokenDTO token = authService.forgotPassword(email);
    	return ResponseEntity.ok()
    			.header(HttpHeaders.AUTHORIZATION, token.accessToken())
    			.body(token);
    }
}
