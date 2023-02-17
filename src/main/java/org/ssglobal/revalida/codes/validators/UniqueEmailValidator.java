package org.ssglobal.revalida.codes.validators;

import org.springframework.stereotype.Component;
import org.ssglobal.revalida.codes.repos.AppUserRepository;
import org.ssglobal.revalida.codes.validators.constraints.UniqueEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final AppUserRepository appUserRepository;

    public UniqueEmailValidator(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return appUserRepository.existsByEmailIgnoreCase(email) == false;
    }

}
