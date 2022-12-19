package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<Mpa> findAll() {
        log.info("Список рейтингов MPA отправлен");
        return mpaStorage.findAll();
    }

    public Mpa getById(int id) {
        if (mpaStorage.getById(id) == null) {
            log.warn("Рейтинг id {} отсутствует", id);
            throw new NotFoundException(String.format("Рейтинг с id: %d отсутствует", id));
        }
        log.info("Отправлен рейтинг с id: {} ", id);
        return mpaStorage.getById(id);
    }
}
