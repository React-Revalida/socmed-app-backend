package org.ssglobal.revalida.codes.controller;

import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final AppUserService appUserService;

    public ProfileController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUserDTO> showMyProfile(@PathVariable String username) {
        AppUserDTO appUserDTO = appUserService.findByUsername(username);
        return new ResponseEntity<>(appUserDTO, HttpStatus.OK);
    }

}
