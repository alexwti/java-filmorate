package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 1;
    private static final LocalDate DATE_BEFORE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film put(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        return null;
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        return null;
    }

    @Override
    public List<Film> getTopRaitingFilms(int count) {
        return null;
    }

    @Override
    public Film addGenre(int filmId, Genre genre) {
        return null;
    }

    @Override
    public Film removeGenre(int filmId, Genre genre) {
        return null;
    }

    @Override
    public List<Integer> getLikes(Integer filmId) {
        return null;
    }

    @Override
    public Film deleteById(int id) {
        Film film = films.get(id);
        films.remove(id);
        return film;
    }
}
