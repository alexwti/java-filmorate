package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film create(Film film);

    public Film put(Film film);

    public Collection<Film> findAll();

    Film deleteById(int id);

    Film getById(int id);
}
