package ru.practicum.ewm.event.validation;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndDateValidator implements ConstraintValidator<StartBeforeEndDateConstraint, HasStartEndRange> {

    @Override
    public void initialize(StartBeforeEndDateConstraint constraint) {
    }

    @Override
    public boolean isValid(HasStartEndRange bean, ConstraintValidatorContext context) {
        final LocalDateTime rangeStart = bean.getRangeStart();
        final LocalDateTime rangeEnd = bean.getRangeEnd();

        if(rangeEnd == null || rangeStart == null) {
            return true;
        }
        return rangeStart.isBefore(rangeEnd);
    }
}
