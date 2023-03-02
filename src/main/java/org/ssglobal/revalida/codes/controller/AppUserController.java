package org.ssglobal.revalida.codes.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.AddressDTO;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.ProfileDTO;
import org.ssglobal.revalida.codes.dto.RegistrationDTO;
import org.ssglobal.revalida.codes.service.AppUserService;
import org.ssglobal.revalida.codes.service.ProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController {

    @Autowired
    private JwtDecoder jwtDecoder;

    private final AppUserService appUserService;

    private final ProfileService profileService;

    public AppUserController(AppUserService appUserService, ProfileService profileService) {
        this.appUserService = appUserService;
        this.profileService = profileService;
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

    @PutMapping(value = "/me/update-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AppUserDTO> updateMyProfile(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
            @RequestPart("user") @Valid final ProfileDTO profileDTO,
            @RequestPart("profile") MultipartFile profilePic,
            @RequestPart("cover") MultipartFile coverPic
            ) throws IOException {
        String jwtToken = token.replace("Bearer ", "");
        Jwt jwt = jwtDecoder.decode(jwtToken);
        String username = jwt.getClaim("user");
        AppUserDTO appUserDTO = profileService.updateProfile(username, profileDTO, profilePic, coverPic);
        return new ResponseEntity<>(appUserDTO, HttpStatus.OK);
    }

    @PutMapping("/me/update-address")
    public ResponseEntity<AppUserDTO> updateMyAddress(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
            @RequestBody @Valid final AddressDTO addressDTO) {
        String jwtToken = token.replace("Bearer ", "");
        Jwt jwt = jwtDecoder.decode(jwtToken);
        String username = jwt.getClaim("user");
        AppUserDTO appUserDTO = profileService.updateOrAddAddress(username, addressDTO);
        return new ResponseEntity<>(appUserDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, String>> getAllUsers(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        String jwtToken = token.replace("Bearer ", "");
        Jwt jwt = jwtDecoder.decode(jwtToken);
        String username = jwt.getClaim("user");
        Map<String, String> namesTbl = appUserService.findAllUsers();
        return new ResponseEntity<>(namesTbl, HttpStatus.OK);
    }

    @PutMapping("/update-password")
    public ResponseEntity<Boolean> updateUserPassword(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
            @RequestBody Map<String, String> data) {
        String jwtToken = token.replace("Bearer ", "");
        Jwt jwt = jwtDecoder.decode(jwtToken);
        String username = jwt.getClaim("user");
        String password = data.get("password");
        Boolean updated = appUserService.updateUserPassword(username, password);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}
