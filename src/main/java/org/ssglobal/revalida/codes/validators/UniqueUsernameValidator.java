package org.ssglobal.revalida.codes.validators;

import org.springframework.stereotype.Component;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.validators.constraints.UniqueUsername;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{

    private final AppUserRepository appUserRepository;

    public UniqueUsernameValidator(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return appUserRepository.existsByUsernameIgnoreCase(username) == false;
    }
    
}
