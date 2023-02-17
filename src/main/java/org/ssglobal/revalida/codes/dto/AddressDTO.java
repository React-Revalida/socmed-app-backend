package org.ssglobal.revalida.codes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddressDTO {

    private Integer addressId;

    @Size(max = 20)
    private String houseNo;

    @NotNull
    @NotBlank
    @Size(max = 20)
    private String street;

    @NotNull
    @NotBlank
    @Size(max = 25)
    private String subdivision;

    @NotNull
    @NotBlank
    @Size(max = 25)
    private String barangay;

    @NotNull
    @NotBlank
    @Size(max = 25)
    private String city;

    @NotNull
    @NotBlank
    @Size(max = 20)
    private String province;

    @NotNull
    private Integer zip;

}
