package ru.practicum.shareit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BeforeValidator implements ConstraintValidator<BeforeNow, LocalDateTime> {


    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value != null) {
            if (!value.isBefore(LocalDateTime.now())) {
                valid = false;
            }
        }
        return valid;
    }
}

