package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Успешно добавлен пользователь с логином: {}, email: {}", user.getLogin(), user.getEmail());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Отсутствует пользоватедб с id: {}", user.getId());
            throw new ValidationException("Ошибка обновления - такого пользователя не существует");
        }
        validate(user);
        users.put(user.getId(), user);
        log.info("Пользователь id: {}, логином: {} успешно обновлен", user.getId(), user.getLogin());
        return user;
    }


    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        Collection<User> userFromCollection = users.values();
        if (userFromCollection.stream().anyMatch(us -> us.getName().equals(user.getName()) ||
                us.getLogin().equals(user.getLogin()))) {
            log.warn("Пользователь email или логин: {}", user);
            throw new ValidationException("Пользователь с таким Email или логином уже существует");
        }
    }
}