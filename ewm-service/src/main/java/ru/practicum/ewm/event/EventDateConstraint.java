package ru.practicum.ewm.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = EventDateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateConstraint {
    String message() default "Дата и время события не могут быть раньше, чем через 2 часа от текущего момента";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}