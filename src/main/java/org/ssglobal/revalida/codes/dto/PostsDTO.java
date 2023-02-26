package org.ssglobal.revalida.codes.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PostsDTO {

    private Integer postId;

    @NotNull
    @NotBlank
    private String message;

    private String imageUrl;

    private String timestamp;

    private Boolean deleted;

    private AppUserDTO user;
    
    private Set<AppUserDTO> likes;
    
    private Set<CommentsDTO> comments;

}
