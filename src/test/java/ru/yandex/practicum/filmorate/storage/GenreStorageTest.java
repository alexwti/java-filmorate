package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {
    private final InDbGenreStorage genreStorage;
    private final InDbFilmStorage filmStorage;

    private final Film film1 = new Film(1,
            "filmname1",
            "filmdescription1",
            LocalDate.now().minusYears(10),
            90,
            4,
            new Mpa(1, "name", "description"),
            new ArrayList<Integer>(),
            new ArrayList<Genre>());

    @Test
    public void findAllTest() {
        Collection<Genre> genre = genreStorage.findAll();
        Assertions.assertThat(genre)
                .extracting(Genre::getName)
                .containsAll(Arrays.asList("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик"));
    }

    @Test
    public void getByIdTest() {
        Genre genre = genreStorage.getById(4);
        AssertionsForClassTypes.assertThat(genre).hasFieldOrPropertyWithValue("name", "Триллер");;
    }

    @Test
    public void getFilmGenres() {
        Film film = filmStorage.create(film1);
        filmStorage.addGenre(film.getId(), genreStorage.getById(4));
        filmStorage.addGenre(film.getId(), genreStorage.getById(3));
        Collection<Genre> list = genreStorage.getFilmGenres(film.getId());
        org.junit.jupiter.api.Assertions.assertEquals(list.size(), 2);
    }
}