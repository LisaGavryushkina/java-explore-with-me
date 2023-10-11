package ru.practicum.ewm.event.validation;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddEventDateValidator implements
        ConstraintValidator<AddEventDateConstraint, LocalDateTime> {

    @Override
    public void initialize(AddEventDateConstraint eventDateConstraint) {
    }

    @Override
    public boolean isValid(LocalDateTime eventDate,
                           ConstraintValidatorContext cxt) {
        return eventDate != null && eventDate.minusHours(2).isAfter(LocalDateTime.now());
    }

}