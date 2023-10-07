package ru.practicum.ewm.event.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DescriptionValidator implements
        ConstraintValidator<DescriptionConstraint, String> {

    @Override
    public void initialize(DescriptionConstraint constraint) {
    }

    @Override
    public boolean isValid(String description,
                           ConstraintValidatorContext cxt) {
        if (description == null) {
            return true;
        }
        return !description.isEmpty() && !description.isBlank() && description.length() >= 20 &&
                description.length() <= 7000;
    }
}
