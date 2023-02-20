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
public class AppUserDTO {

    private Integer userId;

    @UniqueUsername
    @NotNull
    @NotBlank
    @Size(max = 20)
    private String username;

    @UniqueEmail
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String email;

    @NotNull
    @NotBlank
    @Size(max = 255, min = 8, message = "password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "password must have a lowercase letter, uppercase letter, number and special character")
    private String password;

    private Boolean isValidated = true;

    private Boolean isActive = true;

    private Integer profile;

    private String name;

    private String bio;

    private String gender;

    private String profilePic;

    private Integer followers;

    private Integer following;

    private AddressDTO address;
}
