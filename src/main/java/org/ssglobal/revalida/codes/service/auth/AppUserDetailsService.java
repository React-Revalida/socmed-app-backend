package org.ssglobal.revalida.codes.service.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ssglobal.revalida.codes.model.AppUser;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.util.NotFoundException;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getIsValidated()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Boolean validated) {
        return List.of(new SimpleGrantedAuthority(validated ? "USER_VALIDATED" : "USER_NOT_VALIDATED"));
    }
    
    public UserDetails loadUserByEmail(String email) throws Exception {
    	AppUser user = appUserRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email not found"));
    	return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getIsValidated()));
    }

}
