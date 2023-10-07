package ru.practicum.ewm.event.validation;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdateEventDateValidator implements
        ConstraintValidator<UpdateEventDateConstraint, LocalDateTime> {

    @Override
    public void initialize(UpdateEventDateConstraint eventDateConstraint) {
    }

    @Override
    public boolean isValid(LocalDateTime eventDate,
                           ConstraintValidatorContext cxt) {
        if(eventDate == null) {
            return true;
        }
        return  eventDate.minusHours(2).isAfter(LocalDateTime.now());
    }
}
