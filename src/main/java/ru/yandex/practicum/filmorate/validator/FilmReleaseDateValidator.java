package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.annotation.FilmReleaseDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmReleaseDateValidator implements ConstraintValidator<FilmReleaseDate, LocalDate> {
    private static final LocalDate DATE_BEFORE = LocalDate.of(1895, 12, 28);
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        if (value.isBefore(DATE_BEFORE)) {
            return false;
        } else {
            return true;
        }
    }
}
