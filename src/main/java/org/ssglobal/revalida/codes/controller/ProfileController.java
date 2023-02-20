package org.ssglobal.revalida.codes.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.ProfileDTO;
import org.ssglobal.revalida.codes.dto.AddressDTO;
import org.ssglobal.revalida.codes.service.AppUserService;
import org.ssglobal.revalida.codes.service.ProfileService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import jakarta.validation.Valid;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final AppUserService appUserService;
    private final ProfileService profileService;

    @Autowired
    private JwtDecoder jwtDecoder;

    public ProfileController(AppUserService appUserService, ProfileService profileService) {
        this.appUserService = appUserService;
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUserDTO> showMyProfile(@PathVariable String username) {
        AppUserDTO appUserDTO = appUserService.findByUsername(username);
        return new ResponseEntity<>(appUserDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateMyProfile(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
            @RequestPart("user") @Valid final ProfileDTO profileDTO,
            @RequestPart("profile") MultipartFile profilePic) throws IOException {
        String jwtToken = token.replace("Bearer ", "");
        Jwt jwt = jwtDecoder.decode(jwtToken);
        String username = jwt.getClaim("user");
        Boolean isUpdated = profileService.updateProfile(username, profileDTO, profilePic);
        return new ResponseEntity<>(isUpdated, HttpStatus.OK);
    }

    @PostMapping("/update/address")
    public ResponseEntity<Boolean> updateMyAddress(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
            @RequestBody @Valid final AddressDTO addressDTO) {
        String jwtToken = token.replace("Bearer ", "");
        Jwt jwt = jwtDecoder.decode(jwtToken);
        String username = jwt.getClaim("user");
        Boolean isUpdated = profileService.updateOrAddAddress(username, addressDTO);
        return new ResponseEntity<>(isUpdated, HttpStatus.OK);
    }

}
