package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> findAll() {
        log.info("Список жанров отправлен");
        return genreStorage.findAll();
    }

    public Genre getById(int id) {
        if (genreStorage.getById(id) == null) {
            log.warn("Жанр id {} отсутствует", id);
            throw new NotFoundException(String.format("Жанр с id: %d отсутствует", id));
        }
        log.info("Отправлен жанр с id: %d", id);
        return genreStorage.getById(id);
    }

    public Collection<Genre> getFilmGenres(int filmId) {
        return genreStorage.getFilmGenres(filmId);
    }
}

