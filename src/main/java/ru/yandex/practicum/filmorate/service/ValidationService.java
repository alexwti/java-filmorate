package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationService {
    public void userValidate(User user, Collection<User> userFromCollection) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (userFromCollection.stream().anyMatch(us -> us.getName().equals(user.getName()) ||
                us.getLogin().equals(user.getLogin()))) {
            log.warn("Пользователь email или логин: {}", user);
            throw new ValidationException("Пользователь с таким Email или логином уже существует");
        }
    }

}
