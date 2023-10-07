package ru.practicum.ewm.event.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = TitleValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TitleConstraint {
    String message() default "Загловок не должен иметь длину меньше 3 и больше 120 символов";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
