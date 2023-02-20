package org.ssglobal.revalida.codes.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.ssglobal.revalida.codes.dto.AddressDTO;
import org.ssglobal.revalida.codes.dto.ProfileDTO;
import org.ssglobal.revalida.codes.model.Address;
import org.ssglobal.revalida.codes.model.Profile;
import org.ssglobal.revalida.codes.repos.AddressRepository;
import org.ssglobal.revalida.codes.repos.ProfileRepository;
import org.ssglobal.revalida.codes.util.NotFoundException;
import org.ssglobal.revalida.codes.enums.Gender;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;

    private final ImageService imageService;

    @Value("${do.space.profile-dir}")
    private String profileDir;

    public ProfileService(ProfileRepository profileRepository, AddressRepository addressRepository,
            ImageService imageService) {
        this.profileRepository = profileRepository;
        this.addressRepository = addressRepository;
        this.imageService = imageService;
    }

    public boolean updateProfile(String username, ProfileDTO profileDTO, final MultipartFile profilePicture)
            throws IOException {
        Profile profile = profileRepository.findByProfile_Username(username);
        if (profile == null) {
            throw new NotFoundException("Profile not found");
        }
        if (profilePicture != null) {
            imageService.profileUpload(profileDir, profilePicture, profile);
        }
        mapToProfileEntity(profileDTO, profile);
        boolean isUpdated = profileRepository.save(profile) != null;
        return isUpdated;
    }

    @Transactional
    public boolean updateOrAddAddress(String username, AddressDTO addressDTO) {
        Address address = addressRepository.findByAddress_Profile_Username(username);
        if (address == null) {
            // create new address
            address = new Address();
            mapToAddressEntity(addressDTO, address);
            Profile profile = profileRepository.findByProfile_Username(username);
            if (profile == null) {
                throw new NotFoundException("Profile not found");
            }
            profile.setAddress(address);
            boolean isUpdated = addressRepository.save(address) != null;
            if (isUpdated) {
                profileRepository.save(profile);
            }
            return isUpdated;
        }
        mapToAddressEntity(addressDTO, address);
        boolean isUpdated = addressRepository.save(address) != null;
        return isUpdated;
    }

    private Profile mapToProfileEntity(ProfileDTO profileDTO, Profile profile) {
        profile.setFirstname(profileDTO.getFirstname());
        profile.setMiddlename(profileDTO.getMiddlename());
        profile.setLastname(profileDTO.getLastname());
        profile.setGender(Gender.valueOf(profileDTO.getGender()));
        profile.setBirthdate(profileDTO.getBirthdate());
        profile.setPhone(profileDTO.getPhone());
        profile.setDescription(profileDTO.getDescription());
        profile.setProfilePic(profileDTO.getProfilePic());
        return new Profile();
    }

    private Address mapToAddressEntity(AddressDTO addressDTO, Address address) {
        address.setHouseNo(address.getHouseNo());
        address.setStreet(address.getStreet());
        address.setSubdivision(address.getSubdivision());
        address.setBarangay(address.getBarangay());
        address.setCity(address.getCity());
        address.setProvince(address.getProvince());
        address.setZip(address.getZip());
        return address;
    }
}
