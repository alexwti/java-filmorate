package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    final static UserController userController = new UserController();

    @Test
    public void validateEMailTest() {
        final User user = new User("alexwtyandex.ru", "alexwt", LocalDate.now());
        assertThrows(ValidationException.class, () -> userController.validate(user));
        final User user1 = new User("", "alexwt", LocalDate.now());
        assertThrows(ValidationException.class, () -> userController.validate(user1));
    }
    @Test
    public void validateLoginTest() {
        final User user = new User("alexwt@yandex.ru", "al exwt", LocalDate.now());
        assertThrows(ValidationException.class, () -> userController.validate(user));
        final User user1 = new User("alexwt@yandex.ru", "", LocalDate.now());
        assertThrows(ValidationException.class, () -> userController.validate(user1));
    }

    @Test
    public void validateNameTest() {
        final User user = new User("alexwt@yandex.ru", "alexwt", LocalDate.now());
        userController.validate(user);
        assertEquals(user.getName(), "alexwt");
    }

    @Test
    public void validateBirthDayTest() {
        final User user = new User("alexwt@yandex.ru", "alexwt", LocalDate.now().plusDays(10));
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }
}
