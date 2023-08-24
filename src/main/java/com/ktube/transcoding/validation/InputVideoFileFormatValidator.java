package com.ktube.transcoding.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class InputVideoFileFormatValidator implements ConstraintValidator<InputVideoFileFormat, String> {

    Set<String> allowedVideoFormat;

    @Override
    public void initialize(InputVideoFileFormat constraintAnnotation) {

        allowedVideoFormat = Set.of(".mp4", ".mov", ".avi");
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(value == null) return true;

        return allowedVideoFormat.contains(value);
    }
}
