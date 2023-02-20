package org.ssglobal.revalida.codes.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.RegistrationDTO;
import org.ssglobal.revalida.codes.service.AppUserService;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<Boolean> register(@RequestPart("user") @Valid final RegistrationDTO user,
            @RequestPart("profile") MultipartFile profilePic) throws IOException {
        return new ResponseEntity<>(appUserService.createUserProfile(user, profilePic), HttpStatus.CREATED);
    }
}
