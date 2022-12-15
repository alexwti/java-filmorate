package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User put(User user) {
        users.put(user.getId(), user);
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

    @Override
    public List<User> addFriend(int fromId, int toId) {
        return null;
    }

    @Override
    public List<User> deleteFriend(int fromId, int toId) {
        return null;
    }

    @Override
    public List<User> getFriendsById(int id) {
        return null;
    }

    @Override
    public List<User> getCommonFriendsList(int firstUser, int secondUser) {
        return null;
    }
}
