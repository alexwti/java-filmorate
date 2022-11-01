package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private static final LocalDate DATE_BEFORE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм: {} добавлен", film.getName());
        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм id {} отсутствует", film.getId());
            throw new ValidationException("Ошибка обновления - такой фильм отсутствует");
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Фильм: {} обновлен", film.getName());
        return film;
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(DATE_BEFORE)) {
            log.warn("Дата релиза: {}", film.getReleaseDate());
            throw new ValidationException("Некорректная дата релиза фильма");
        }

        if (film.getName()==null || film.getName().isBlank()) {
            log.warn("Наименование фильма пустое: {}", film.getDuration());
            throw new ValidationException("Наименование фильма должно быть заполнено");
        }

        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть меньше нуля");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Длина описания фильма: {}", film.getDescription());
            throw new ValidationException("Превышена максимальная длина описание в 200 символов");
        }

        Collection<Film> filmFromCollection = films.values();
        if (filmFromCollection.stream().anyMatch(fl -> fl.getName().equals(film.getName()) &&
                fl.getReleaseDate().equals(film.getReleaseDate()))) {
            log.warn("Фильм к добавлению: ", film);
            throw new ValidationException("Такой фильм уже существует в коллекции");
        }
    }
}