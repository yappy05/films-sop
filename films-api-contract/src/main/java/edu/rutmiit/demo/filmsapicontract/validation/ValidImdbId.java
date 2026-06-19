package edu.rutmiit.demo.filmsapicontract.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Аннотация для валидации IMDb ID (формат tt1234567).
 *
 * null и пустая строка считаются корректными: за обязательность отвечает @NotBlank.
 *
 * Примеры валидных значений:
 *   tt1375666  — Inception
 *   tt0468569  — The Dark Knight
 */
@Documented
@Constraint(validatedBy = ImdbIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImdbId {

    String message() default "Некорректный IMDb ID. Допустимый формат: tt + 7–8 цифр";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
