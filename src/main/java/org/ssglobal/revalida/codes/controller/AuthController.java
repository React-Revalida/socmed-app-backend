package org.ssglobal.revalida.codes.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.records.AuthenticationDTO;
import org.ssglobal.revalida.codes.dto.records.TokenDTO;
import org.ssglobal.revalida.codes.service.auth.AppUserDetailsService;
import org.ssglobal.revalida.codes.service.auth.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AppUserDetailsService appUserDetailsService;
    private final AuthService authService;

    public AuthController(AppUserDetailsService appUserDetailsService, AuthService authService) {
        this.appUserDetailsService = appUserDetailsService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> login(@RequestBody AuthenticationDTO user) {
        TokenDTO token = authService.login(user.username(), user.password());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token.accessToken())
                .body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody @Valid final AppUserDTO user) {
        return new ResponseEntity<>(appUserDetailsService.create(user), HttpStatus.CREATED);
    }
}
