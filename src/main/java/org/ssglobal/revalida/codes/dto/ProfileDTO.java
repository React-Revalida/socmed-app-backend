package org.ssglobal.revalida.codes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProfileDTO {

    private Integer profileId;

    @NotNull
    @Size(max = 50)
    private String firstname;

    @Size(max = 20)
    private String middlename;

    @NotNull
    @Size(max = 50)
    private String lastname;

    @NotNull
    private Gender gender;

    private LocalDate birthdate;

    private LocalDate registerdate;

    @Size(max = 11)
    private String phone;

    @Size(max = 160)
    private String description;

    private String profilePic;

    private Integer address;

}
