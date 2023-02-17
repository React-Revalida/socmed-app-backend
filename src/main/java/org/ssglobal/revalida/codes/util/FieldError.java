package org.ssglobal.revalida.codes.util;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FieldError {

    private String field;
    private String message;
    private String errorCode;

}
