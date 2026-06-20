package edu.rutmiit.demo.filmsapicontract.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImdbIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImdbId {

    String message() default "Некорректный IMDb ID. Допустимый формат: tt + 7–8 цифр";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
