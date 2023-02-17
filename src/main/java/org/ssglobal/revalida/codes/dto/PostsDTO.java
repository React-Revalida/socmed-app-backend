package org.ssglobal.revalida.codes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PostsDTO {

    private Integer postId;

    @NotNull
    private String message;

    private String imageUrl;

    private String timestamp;

    private Boolean deleted;

    private Integer user;

}
