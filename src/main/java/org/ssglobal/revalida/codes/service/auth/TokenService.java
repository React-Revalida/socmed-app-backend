package org.ssglobal.revalida.codes.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.repos.AppUserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final AppUserRepository appUserRepository;

    public TokenService(JwtEncoder jwtEncoder, AppUserRepository appUserRepository) {
        this.jwtEncoder = jwtEncoder;
        this.appUserRepository = appUserRepository;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Integer userId = appUserRepository.findByUsername(authentication.getName()).get().getUserId();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("user", authentication.getName())
                .claim("userId", userId)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    public String generateResetToken(UserDetails userDetails) {
    	Instant now = Instant.now();
    	Integer userId = appUserRepository.findByUsername(userDetails.getUsername()).get().getUserId();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(userDetails.getUsername())
                .claim("user", userDetails.getUsername())
                .claim("userId", userId)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
   
}
