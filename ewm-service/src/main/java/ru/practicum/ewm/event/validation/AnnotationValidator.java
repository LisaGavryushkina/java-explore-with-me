package ru.practicum.ewm.event.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnnotationValidator implements
        ConstraintValidator<AnnotationConstraint, String> {

    @Override
    public void initialize(AnnotationConstraint constraint) {
    }

    @Override
    public boolean isValid(String annotation,
                           ConstraintValidatorContext cxt) {
        if (annotation == null) {
            return true;
        }
        return !annotation.isEmpty() && !annotation.isBlank() && annotation.length() >= 20 &&
                annotation.length() <= 2000;
    }
}
