package com.ktube.transcoding.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InputVideoFileFormatValidator.class)
public @interface InputVideoFileFormat {

    String message() default "requestVideoFileExtension is not allowed by this server policy";
    Class[] groups() default {};
    Class[] payload() default {};
}
