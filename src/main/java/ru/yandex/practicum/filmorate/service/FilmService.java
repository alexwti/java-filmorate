package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    public Film addLike(int filmId, int userId) {
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        filmStorage.getById(filmId).getLikesUserID().add(userId);
        log.info("Пользователь id: {} поставил лайк фильму id {}", userId, filmId);
        return filmStorage.getById(filmId);
    }

    public Film removeLike(int filmId, int userId) {
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!filmStorage.getById(filmId).getLikesUserID().contains(userId)) {
            throw new NotFoundException("Лайка от такого пользователя нет");
        }
        filmStorage.getById(filmId).getLikesUserID().remove(userId);
        log.info("Пользователь id: {} удалил лайк фильму id {}", userId, filmId);
        return filmStorage.getById(filmId);
    }

    public List<Film> getTopRaitingFilms(int count) {
        log.info("Отправлен список {} популярных фильмов", count);
        return filmStorage.findAll().stream().sorted((o1, o2) -> Integer.compare(o2.getLikesUserID().size(), o1.getLikesUserID().size())).limit(count).collect(Collectors.toList());
    }
}
