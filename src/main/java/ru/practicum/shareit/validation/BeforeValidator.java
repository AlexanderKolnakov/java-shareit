package ru.practicum.shareit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BeforeValidator implements ConstraintValidator<BeforeNow, LocalDate> {


    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value != null) {
            if (!value.isBefore(LocalDate.now())) {
                valid = false;
            }
        }
        return valid;
    }
}

