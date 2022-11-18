package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();
    private final ValidationService validationService;

    @Override
    public User create(User user) {
        validationService.userValidate(user, findAll());
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Успешно добавлен пользователь с логином: {}, email: {}", user.getLogin(), user.getEmail());
        return user;
    }

    @Override
    public User put(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Отсутствует пользоватедб с id: {}", user.getId());
            throw new ValidationException("Ошибка обновления - такого пользователя не существует");
        }
        validationService.userValidate(user, findAll());
        users.put(user.getId(), user);
        log.info("Пользователь id: {}, логином: {} успешно обновлен", user.getId(), user.getLogin());
        return user;
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }

    @Override
    public User deleteById(int id) {
        User user = users.get(id);
        users.remove(id);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }
}
