package org.ssglobal.revalida.codes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentsDTO {

    private Integer id;

    @NotNull
    @NotBlank
    private String message;

    private String timestamp;
 
    private AppUserDTO user;

    private Integer post;

}
