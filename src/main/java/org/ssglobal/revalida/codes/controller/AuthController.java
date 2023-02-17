package org.ssglobal.revalida.codes.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.records.AuthenticationDTO;
import org.ssglobal.revalida.codes.dto.records.TokenDTO;
import org.ssglobal.revalida.codes.service.auth.AppUserDetailsService;
import org.ssglobal.revalida.codes.service.auth.TokenService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;

    public AuthController(TokenService tokenService,
            AuthenticationManager authenticationManager,
            AppUserDetailsService appUserDetailsService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.appUserDetailsService = appUserDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> token(@RequestBody AuthenticationDTO user) {
        LOG.info("Token request for user {}", user.username());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.username(), user.password()));
        String token = tokenService.generateToken(authentication);
        LOG.info("Token generated for user {}", user.username());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(new TokenDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerShopUser(@RequestBody @Valid final AppUserDTO user) {
        return new ResponseEntity<>(appUserDetailsService.create(user), HttpStatus.CREATED);
    }
}
