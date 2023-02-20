package org.ssglobal.revalida.codes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProfileDTO {

    private Integer profileId;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String firstname;

    @Size(max = 20)
    private String middlename;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String lastname;

    @NotNull
    private Gender gender;

    private LocalDate birthdate;

    @Size(min = 11, max = 11, message = "contact number must be 11 digits")
    @Pattern(regexp = "^(09)\\d{9}$", message = "contact number must start with 09 and must be 11 digits")
    private String phone;

    @Size(max = 160)
    private String description;

    private String profilePic;

    private Integer address;

}
