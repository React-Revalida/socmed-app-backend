package org.ssglobal.revalida.codes.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.RegistrationDTO;
import org.ssglobal.revalida.codes.enums.Gender;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Profile;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.ProfileRepository;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AppUserService {
    private static final Logger LOG = LoggerFactory.getLogger(AppUserService.class);

    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final ProfileRepository profileRepository;

    private final ImageService imageService;

    public AppUserService(PasswordEncoder passwordEncoder, AppUserRepository appUserRepository,
            ProfileRepository profileRepository, ImageService imageService) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.profileRepository = profileRepository;
        this.imageService = imageService;
    }

    @Transactional
    public boolean createUserProfile(final RegistrationDTO registrationDTO, final MultipartFile profilePicture)
            throws IOException {
        final Profile profile = new Profile();
        if (profilePicture != null) {
            imageService.profileUpload(profilePicture, profile);
        }
        mapToAppProfileRegistrationEntity(registrationDTO, profile);
        Profile savedProfile = profileRepository.save(profile);
        if (savedProfile != null) {
            createUser(registrationDTO, savedProfile);
            return true;
        }
        return false;
    }

    @Transactional
    public void createUser(final RegistrationDTO registrationDTO, final Profile savedProfile) {
        final AppUser appUser = new AppUser();
        mapToAppUserRegistrationEntity(registrationDTO, appUser);
        LOG.info("Creating user {}", registrationDTO.getUsername());
        appUser.setProfile(savedProfile);
        appUserRepository.save(appUser);
        LOG.info("User {} created", appUser.getUsername());
    }

    private AppUser mapToAppUserRegistrationEntity(RegistrationDTO registrationDTO, AppUser appUser) {
        appUser.setUsername(registrationDTO.getUsername());
        appUser.setEmail(registrationDTO.getEmail());
        appUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        appUser.setIsActive(registrationDTO.getIsActive());
        appUser.setIsValidated(registrationDTO.getIsValidated());
        return appUser;
    }

    private Profile mapToAppProfileRegistrationEntity(RegistrationDTO registrationDTO, Profile profile) {
        profile.setFirstname(registrationDTO.getFirstname());
        profile.setMiddlename(registrationDTO.getMiddlename());
        profile.setLastname(registrationDTO.getLastname());
        profile.setGender(Gender.valueOf(registrationDTO.getGender()));
        return profile;
    }

}
