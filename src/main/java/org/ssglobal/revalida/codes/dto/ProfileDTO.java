package org.ssglobal.revalida.codes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.micrometer.common.lang.Nullable;
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
    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    private String phone;

    @Size(max = 160)
    private String bio;

    private String profilePic;

    private Integer address;

}
