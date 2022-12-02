package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
    final static ValidationService validationService = new ValidationService();
    final static UserStorage userStorage = new InMemoryUserStorage();
    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    public void validateEMailTest() {
        final User user = new User("alexwtyandex.ru", "alexwt", LocalDate.now());
        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertTrue(validates.size() > 0);
        validates.stream().map(v -> v.getMessage()).forEach(System.out::println);
        final User user1 = new User("", "alexwt", LocalDate.now());
        Set<ConstraintViolation<User>> validates1 = validator.validate(user1);
        assertTrue(validates1.size() > 0);
        validates1.stream().map(v -> v.getMessage()).forEach(System.out::println);
    }

    @Test
    public void validateLoginTest() {
        final User user = new User("alexwt@yandex.ru", "al exwt", LocalDate.now());
        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertTrue(validates.size() > 0);
        validates.stream().map(v -> v.getMessage()).forEach(System.out::println);
        final User user1 = new User("alexwt@yandex.ru", "", LocalDate.now());
        Set<ConstraintViolation<User>> validates1 = validator.validate(user1);
        assertTrue(validates1.size() > 0);
        validates1.stream().map(v -> v.getMessage()).forEach(System.out::println);
    }

    @Test
    public void validateNameTest() {
        final User user = new User("alexwt@yandex.ru", "alexwt", LocalDate.now());
        validationService.userValidate(user, userStorage.findAll());
        assertEquals(user.getName(), "alexwt");
    }

    @Test
    public void validateBirthDayTest() {
        final User user = new User("alexwt@yandex.ru", "alexwt", LocalDate.now().plusDays(10));
        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertTrue(validates.size() > 0);
        validates.stream().map(v -> v.getMessage()).forEach(System.out::println);
    }
}
