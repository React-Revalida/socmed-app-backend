package org.ssglobal.revalida.codes.dto;

import org.ssglobal.revalida.codes.validators.constraints.UniqueEmail;
import org.ssglobal.revalida.codes.validators.constraints.UniqueUsername;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {

    @NotNull(message = "firstname cannot be blank")
    @NotBlank(message = "firstname cannot be blank")
    @Size(max = 50)
    private String firstname;

    @Size(max = 20)
    private String middlename;

    @NotNull(message = "lastname cannot be blank")
    @NotBlank(message = "lastname cannot be blank")
    @Size(max = 50)
    private String lastname;

    @NotNull
    private String gender;

    @UniqueUsername
    @NotNull(message = "username cannot be blank")
    @NotBlank(message = "username cannot be blank")
    @Size(max = 20)
    private String username;

    @UniqueEmail
    @NotNull(message = "email cannot be blank")
    @NotBlank(message = "email cannot be blank")
    @Size(max = 50)
    private String email;

    @NotNull(message = "password cannot be blank")
    @NotBlank(message = "password cannot be blank")
    @Size(max = 255, min = 8, message = "password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "password must have a lowercase letter, uppercase letter, number and special character")
    private String password;

    private Boolean isValidated = true;

    private Boolean isActive = true;
}