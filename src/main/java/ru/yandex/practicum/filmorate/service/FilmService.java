package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    public Film create(Film film) {
        log.info("Фильм: {} добавлен", film.getName());
        return filmStorage.create(film);
    }
    public Film put(Film film) {
        if (filmStorage.getById(film.getId()) == null) {
            log.warn("Фильм id {} отсутствует", film.getId());
            throw new NotFoundException(String.format("Ошибка обновления - фильм с id: %d отсутствует", film.getId()));
        }
        log.info("Фильм: {} обновлен", film.getName());
        return filmStorage.put(film);
    }

    public Collection<Film> findAll() {
        log.info("Список фильмов отправлен");
        return filmStorage.findAll();
    }

    public Film getById(int id) {
        if (filmStorage.getById(id) == null) {
            log.warn("Фильм id {} отсутствует", id);
            throw new NotFoundException(String.format("Фильм с id: %d отсутствует", id));
        }
        log.info("Отправлен фильм с id: {} ", id);
        return filmStorage.getById(id);
    }

    public Film deleteById(int id) {
        if (filmStorage.getById(id) == null) {
            throw new NotFoundException(String.format("Фильм с id: %d отсутствует, удаление невозможно", id));
        }
        log.info("Фильм с id: {} удалён", id);
        return filmStorage.deleteById(id);
    }

    public Film addLike(int filmId, int userId) {
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        log.info("Пользователь id: {} поставил лайк фильму id {}", userId, filmId);
        return filmStorage.addLike(filmId, userId);
    }

    public Film removeLike(int filmId, int userId) {
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!filmStorage.getById(filmId).getLikesUserID().contains(userId)) {
            throw new NotFoundException("Лайка от такого пользователя нет");
        }
        log.info("Пользователь id: {} удалил лайк фильму id {}", userId, filmId);
        return filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getTopRaitingFilms(int count) {
        log.info("Отправлен список {} популярных фильмов", count);
        return filmStorage.getTopRaitingFilms(count);
    }
}
