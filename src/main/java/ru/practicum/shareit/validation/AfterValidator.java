package ru.practicum.shareit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.Optional;

public class AfterValidator implements ConstraintValidator<AfterNow, LocalDateTime> {

    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value != null) {
            if (!value.isAfter(LocalDateTime.now())) {
                valid = false;
            }
        }
        return valid;
    }
}


