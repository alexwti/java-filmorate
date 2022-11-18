package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

public interface UserStorage {
    public User create(User user);

    public User put(User user);

    User getById(int id);

    User deleteById(int id);
    public Collection<User> findAll();
}
