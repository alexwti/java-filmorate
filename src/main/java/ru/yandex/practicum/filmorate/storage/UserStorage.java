package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    public User create(User user);

    public User put(User user);

    User getById(int id);

    User deleteById(int id);

    public Collection<User> findAll();

    List<User> addFriend(int fromId, int toId);

    List<User> deleteFriend(int fromId, int toId);

    List<User> getFriendsById(int id);

    List<User> getCommonFriendsList(int firstUser, int secondUser);
}
