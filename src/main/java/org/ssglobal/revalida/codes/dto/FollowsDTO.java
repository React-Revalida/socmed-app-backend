package org.ssglobal.revalida.codes.dto;

import org.ssglobal.revalida.codes.validators.constraints.UniqueUsername;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowsDTO {
	
    private Integer userId;

    @UniqueUsername
    @NotNull
    @NotBlank
    @Size(max = 20)
    private String username;
    
    private String profilePic;

}