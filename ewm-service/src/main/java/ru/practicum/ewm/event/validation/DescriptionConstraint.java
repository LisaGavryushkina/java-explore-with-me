package ru.practicum.ewm.event.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DescriptionValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DescriptionConstraint {
    String message() default "Описание не должно быть пустым, состоять из пробелов, иметь длину меньше 20 и больше " +
            "7000 символов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}