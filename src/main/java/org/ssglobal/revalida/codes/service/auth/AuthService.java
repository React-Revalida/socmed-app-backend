package org.ssglobal.revalida.codes.service.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.dto.records.TokenDTO;

@Service
public class AuthService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;

    public AuthService(TokenService tokenService, AuthenticationManager authenticationManager, AppUserDetailsService appUserDetailsService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.appUserDetailsService = appUserDetailsService;
    }

    public TokenDTO login(String username, String password) {
        LOG.info("Token request for user {}", username);
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities()));
        String token = tokenService.generateToken(authentication);
        LOG.info("Token generated for user {}", authentication.getName());
        return new TokenDTO(token);
    }
}
