package ru.practicum.ewm.event.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TitleValidator implements
        ConstraintValidator<TitleConstraint, String> {

    @Override
    public void initialize(TitleConstraint constraint) {
    }

    @Override
    public boolean isValid(String title,
                           ConstraintValidatorContext cxt) {
        if (title == null) {
            return true;
        }
        return title.length() >= 3 && title.length() <= 120;
    }
}
