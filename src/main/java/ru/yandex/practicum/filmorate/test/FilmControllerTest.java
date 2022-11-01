package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    final static FilmController filmController = new FilmController();

    @Test
    public void validateNameTest() {
        Film film = new Film("", "description", LocalDate.now(), 1);
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    public void validateDescriptionTest() {
        Film film = new Film("", String.format("%1$220s", ""), LocalDate.now(), 1);
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    public void validateReleaseDateTest() {
        Film film = new Film("name", "description", LocalDate.of(1795, 12, 28), 1);
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    public void validateDurationTest() {
        Film film = new Film("name", "description", LocalDate.now(), -1);
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }
}
