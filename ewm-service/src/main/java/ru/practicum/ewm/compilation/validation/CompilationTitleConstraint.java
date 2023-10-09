package ru.practicum.ewm.compilation.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = CompilationTitleValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompilationTitleConstraint {
    String message() default "Название не может иметь длину больше 50 символов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
