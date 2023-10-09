package ru.practicum.ewm.compilation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompilationTitleValidator implements
        ConstraintValidator<CompilationTitleConstraint, String> {

    @Override
    public void initialize(CompilationTitleConstraint constraint) {
    }

    @Override
    public boolean isValid(String title,
                           ConstraintValidatorContext cxt) {
        if (title == null) {
            return true;
        }
        return title.length() <= 50;
    }
}
