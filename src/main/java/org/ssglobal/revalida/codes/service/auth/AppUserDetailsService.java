package org.ssglobal.revalida.codes.service.auth;

import org.ssglobal.revalida.codes.dto.AppUserDTO;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(AppUserDetailsService.class);

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public AppUserDetailsService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean create(final AppUserDTO appUserDTO) {
        final AppUser appUser = new AppUser();
        LOG.info("Creating user {}", appUserDTO.getUsername());
        mapToEntity(appUserDTO, appUser);
        boolean isCreated = appUserRepository.save(appUser) != null;
        if (isCreated) {
            LOG.info("User {} created", appUserDTO.getUsername());
        }
        return isCreated;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getIsActive()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Boolean active) {
        return List.of(new SimpleGrantedAuthority(active ? "USER_ACTIVE" : "USER_INACTIVE"));
    }

    private AppUser mapToEntity(final AppUserDTO appUserDTO, final AppUser appUser) {
        appUser.setUsername(appUserDTO.getUsername());
        appUser.setEmail(appUserDTO.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        appUser.setIsActive(appUserDTO.getIsActive());
        appUser.setIsValidated(appUserDTO.getIsValidated());
        return appUser;
    }
}
