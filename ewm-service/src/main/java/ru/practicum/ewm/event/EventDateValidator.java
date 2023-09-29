package ru.practicum.ewm.event;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventDateValidator implements
        ConstraintValidator<EventDateConstraint, LocalDateTime> {

    @Override
    public void initialize(EventDateConstraint eventDateConstraint) {
    }

    @Override
    public boolean isValid(LocalDateTime eventDate,
      ConstraintValidatorContext cxt) {
        return eventDate != null && eventDate.minusHours(2).isAfter(LocalDateTime.now());
    }

}