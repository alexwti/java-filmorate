package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTest {
    final static FilmStorage filmStorage = new InMemoryFilmStorage();
    final static FilmService filmService = new FilmService(filmStorage);
    final static FilmController filmController = new FilmController(filmStorage, filmService);
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    @Test
    public void validateNameTest() {
        Film film = new Film("", "description", LocalDate.now(), 1);
        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);
        validates.stream().map(v -> v.getMessage()).forEach(System.out::println);
    }

    @Test
    public void validateDescriptionTest() {
        Film film = new Film("", String.format("%1$220s", ""), LocalDate.now(), 1);
        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);
        validates.stream().map(v -> v.getMessage()).forEach(System.out::println);
    }

    @Test
    public void validateReleaseDateTest() {
        Film film = new Film("name", "description", LocalDate.of(1795, 12, 28), 1);
        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);
        validates.stream().map(v -> v.getMessage()).forEach(System.out::println);
    }

    @Test
    public void validateDurationTest() {
        Film film = new Film("name", "description", LocalDate.now(), -1);
        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);
        validates.stream().map(v -> v.getMessage()).forEach(System.out::println);
    }
}
