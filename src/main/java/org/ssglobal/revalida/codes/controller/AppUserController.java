package org.ssglobal.revalida.codes.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.RegistrationDTO;
import org.ssglobal.revalida.codes.service.AppUserService;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController {

    @Autowired
    private JwtDecoder jwtDecoder;

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<Boolean> register(@RequestPart("user") @Valid final RegistrationDTO user,
            @RequestPart("profile") MultipartFile profilePic) throws IOException {
        return new ResponseEntity<>(appUserService.createUserProfile(user, profilePic), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<AppUserDTO> showMyProfile(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token)
            throws ParseException {
        String jwtToken = token.replace("Bearer ", "");
        Jwt jwt = jwtDecoder.decode(jwtToken);
        String user = jwt.getClaim("user");
        AppUserDTO appUserDTO = appUserService.findByUsername(user);
        return new ResponseEntity<>(appUserDTO, HttpStatus.OK);
    }
}
