package org.ssglobal.revalida.codes.dto;

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
    @Size(max = 20)
    private String street;

    @NotNull
    @Size(max = 25)
    private String subdivision;

    @NotNull
    @Size(max = 25)
    private String barangay;

    @NotNull
    @Size(max = 25)
    private String city;

    @NotNull
    @Size(max = 20)
    private String province;

    @NotNull
    private Integer zip;

}
