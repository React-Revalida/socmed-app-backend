package org.ssglobal.revalida.codes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.AddressDTO;
import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.dto.RegistrationDTO;
import org.ssglobal.revalida.codes.enums.Gender;
import org.ssglobal.revalida.codes.model.Address;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.model.Profile;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.repos.FollowsRepository;
import org.ssglobal.revalida.codes.repos.ProfileRepository;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AppUserService {
    private static final Logger LOG = LoggerFactory.getLogger(AppUserService.class);

    @Value("${do.space.profile-dir}")
    private String profileDir;

    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final ProfileRepository profileRepository;
    private final FollowsRepository followsRepository;

    private final ImageService imageService;

    public AppUserService(PasswordEncoder passwordEncoder, AppUserRepository appUserRepository,
            ProfileRepository profileRepository, FollowsRepository followsRepository, ImageService imageService) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.profileRepository = profileRepository;
        this.followsRepository = followsRepository;
        this.imageService = imageService;
    }

    @Transactional
    public boolean createUserProfile(final RegistrationDTO registrationDTO, final MultipartFile profilePicture)
            throws IOException {
        final Profile profile = new Profile();
        if (profilePicture != null) {
            imageService.profileUpload(profileDir, profilePicture, profile);
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

    public AppUserDTO findByUsername(String username) {
        AppUser appUser = appUserRepository.findByUsernameIgnoreCase(username);
        return mapToAppUserDTO(appUser, new AppUserDTO());
    }

    private AppUserDTO mapToAppUserDTO(AppUser appUser, AppUserDTO appUserDTO) {
        appUserDTO.setUserId(appUser.getUserId());
        appUserDTO.setFirstname(appUser.getProfile().getFirstname());
        appUserDTO.setMiddlename(appUser.getProfile().getMiddlename());
        appUserDTO.setLastname(appUser.getProfile().getLastname());
        appUserDTO.setPhone(appUser.getProfile().getPhone());
        appUserDTO.setBirthdate(appUser.getProfile().getBirthdate());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setIsActive(appUser.getIsActive());
        appUserDTO.setIsValidated(appUser.getIsValidated());
        appUserDTO.setProfile(appUser.getProfile().getProfileId());
        String name = appUser.getProfile().getMiddlename() != null
                ? String.format("%s %s %s", appUser.getProfile().getFirstname(), appUser.getProfile().getMiddlename(),
                        appUser.getProfile().getLastname())
                : String.format("%s %s", appUser.getProfile().getFirstname(), appUser.getProfile().getLastname());
        appUserDTO.setName(name);
        appUserDTO.setBio(appUser.getProfile().getDescription());
        appUserDTO.setGender(appUser.getProfile().getGender().toString());
        appUserDTO.setProfilePic(imageService.getImageUrl(appUser.getProfile().getProfilePic()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String dateJoined = appUser.getDateCreated().format(formatter);
        appUserDTO.setDateJoined(dateJoined);
        final Integer followers = followsRepository.countFollowersByUserUsername(appUser.getUsername());
        final Integer following = followsRepository.countFollowingByUserUsername(appUser.getUsername());
        appUserDTO.setFollowers(followers);
        appUserDTO.setFollowing(following);
        appUserDTO.setAddress(mapToAddressDTO(appUser.getProfile().getAddress()));
        return appUserDTO;
    }

    private AddressDTO mapToAddressDTO(Address address) {
        if (address != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setAddressId(address.getAddressId());
            addressDTO.setHouseNo(address.getHouseNo());
            addressDTO.setStreet(address.getStreet());
            addressDTO.setSubdivision(address.getSubdivision());
            addressDTO.setBarangay(address.getBarangay());
            addressDTO.setCity(address.getCity());
            addressDTO.setProvince(address.getProvince());
            addressDTO.setZip(address.getZip());
            return addressDTO;
        }
        return null;
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
