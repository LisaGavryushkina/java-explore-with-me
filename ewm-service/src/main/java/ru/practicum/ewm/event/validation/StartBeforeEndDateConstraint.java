package ru.practicum.ewm.event.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = StartBeforeEndDateValidator.class)
@Documented

public @interface StartBeforeEndDateConstraint {

    String message() default "{Временной промежуток для выборки событий некорректен}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
