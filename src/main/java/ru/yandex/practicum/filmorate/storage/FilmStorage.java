package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FilmStorage {
    public Film create(Film film);

    public Film put(Film film);

    public Collection<Film> findAll();

    Film deleteById(int id);

    Film getById(int id);

    Film addLike(int filmId, int userId);

    Film removeLike(int filmId, int userId);

    List<Film> getTopRaitingFilms(int count);

    Film addGenre(int filmId, Genre genre);

    Film removeGenre(int filmId, Genre genre);

    List<Integer> getLikes(Integer filmId);

}
