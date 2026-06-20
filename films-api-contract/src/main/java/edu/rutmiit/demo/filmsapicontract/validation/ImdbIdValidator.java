package edu.rutmiit.demo.filmsapicontract.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ImdbIdValidator implements ConstraintValidator<ValidImdbId, String> {

    private static final Pattern IMDB_ID = Pattern.compile("^tt\\d{7,8}$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return IMDB_ID.matcher(value.trim()).matches();
    }
}
