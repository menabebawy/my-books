package com.mybooks.clientservice.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberConstraintValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.isBlank() &&
                (value.length() > 8) &&
                (value.length() < 20);
    }
}
