package org.ssglobal.revalida.codes.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LikesDTO {

    private Integer id;
    private Boolean liked;
    private Integer user;
    private Integer post;

}
 