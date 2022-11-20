package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final ValidationService validationService;

    public User create(User user) {
        validationService.userValidate(user, findAll());
        log.info("Успешно добавлен пользователь с логином: {}, email: {}", user.getLogin(), user.getEmail());
        return userStorage.create(user);
    }

    public User put(User user) {
        if (userStorage.getById(user.getId()) == null) {
            log.warn("Отсутствует пользователь с id: {}", user.getId());
            throw new NotFoundException("Ошибка обновления - такого пользователя не существует");
        }
        validationService.userValidate(user, findAll());
        log.info("Пользователь id: {}, логином: {} успешно обновлен", user.getId(), user.getLogin());
        return userStorage.put(user);
    }

    public Collection<User> findAll() {
        log.info("Список пользователей отправлен");
        return userStorage.findAll();
    }

    public User getById(int id) {
        if (userStorage.getById(id) == null) {
            log.warn("Отсутствует пользователь с id: {}", id);
            throw new NotFoundException(String.format("Пользователя с id %d не существует", id));
        }
        log.info("Пользователь с id: '{}' отправлен", id);
        return userStorage.getById(id);
    }

    public User deleteById(int id) {
        if (userStorage.getById(id) == null) {
            log.warn("Отсутствует пользователь с id: {}", id);
            throw new NotFoundException(String.format("Пользователя с id %d не существует", id));
        }
        log.info("Пользователь с id: '{}' удален", id);
        return userStorage.deleteById(id);
    }

    public List<User> addFriend(int fromId, int toId) {
        if (userStorage.getById(fromId) == null || userStorage.getById(toId) == null) {
            throw new NotFoundException(String.format("Не существует пользователей с id: %d или %d", fromId, toId));
        }
        if (userStorage.getById(fromId).getFriendsID().contains(toId) || userStorage.getById(toId).getFriendsID().contains(fromId)) {
            throw new ValidationException("Пользователи уже друзья");
        }
        userStorage.getById(fromId).getFriendsID().add(toId);
        userStorage.getById(toId).getFriendsID().add(fromId);
        log.info("Пользователи: '{}' и '{}' добавлены в  друзья )", userStorage.getById(fromId).getName(), userStorage.getById(toId).getName());
        return Arrays.asList(userStorage.getById(fromId), userStorage.getById(toId));
    }

    public List<User> deleteFriend(int fromId, int toId) {
        if (userStorage.getById(fromId) == null || userStorage.getById(toId) == null) {
            throw new NotFoundException(String.format("Не существует пользователей с id: %d или %d", fromId, toId));
        }
        if (!userStorage.getById(fromId).getFriendsID().contains(toId) || !userStorage.getById(toId).getFriendsID().contains(fromId)) {
            throw new ValidationException("Пользователи не являются друзьями");
        }
        userStorage.getById(fromId).getFriendsID().remove(toId);
        userStorage.getById(toId).getFriendsID().remove(fromId);
        log.info("Пользователи: '{}' и '{}' удалены из друзей :(", userStorage.getById(fromId).getName(), userStorage.getById(toId).getName());
        return Arrays.asList(userStorage.getById(fromId), userStorage.getById(toId));
    }

    public List<User> getFriendsById(int id) {
        if (userStorage.getById(id) == null) {
            throw new NotFoundException(String.format("Не существует пользователя с id: %d", id));
        }
        log.info("Cписок друзей пользователя {} :)", userStorage.getById(id).getName());
        return userStorage.getById(id).getFriendsID().stream().map(userStorage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriendsList(int fromId, int toId) {
        if (userStorage.getById(fromId) == null || userStorage.getById(toId) == null) {
            throw new NotFoundException(String.format("Не существует пользователей с id: %d или %d", fromId, toId));
        }
        log.info("Cписок общих друзей пользователей {} и {} :)", userStorage.getById(fromId).getName(), userStorage.getById(toId).getName());

        return userStorage.getById(fromId).getFriendsID().stream().filter(friendsId -> userStorage.getById(toId).getFriendsID().contains(friendsId)).map(userStorage::getById).collect(Collectors.toList());
    }
}
