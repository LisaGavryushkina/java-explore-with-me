package ru.practicum.ewm.event.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = AnnotationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationConstraint {
    String message() default "Аннотация не должна быть пустой, состоять из пробелов, иметь длину меньше 20 и больше " +
            "2000 символов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
