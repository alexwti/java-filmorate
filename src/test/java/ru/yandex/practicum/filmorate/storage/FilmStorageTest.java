package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    private final InDbFilmStorage filmStorage;
    private final InDbUserStorage userStorage;

    private User user1 = new User(1,
            "1@yandex.ru",
            "login1",
            "Name1",
            LocalDate.of(1980, 9, 28),
            new ArrayList<>());
    private User user2 = new User(2,
            "2@yandex.ru",
            "login2",
            "Name2",
            LocalDate.of(1980, 9, 28),
            new ArrayList<>());
    private User user3 = new User(3,
            "3@yandex.ru",
            "login3",
            "Name3",
            LocalDate.of(1980, 9, 28),
            new ArrayList<>());

    private User user4 = new User(4,
            "4@yandex.ru",
            "login4",
            "Name4",
            LocalDate.of(1980, 9, 28),
            new ArrayList<>());
    private User user5 = new User(5,
            "5@yandex.ru",
            "login5",
            "Name5",
            LocalDate.of(1980, 9, 28),
            new ArrayList<>());

    private User user6 = new User(6,
            "6@yandex.ru",
            "login6",
            "Name6",
            LocalDate.of(1980, 9, 28),
            new ArrayList<>());

    private final Film film1 = new Film(1,
            "filmname1",
            "filmdescription1",
            LocalDate.now().minusYears(10),
            90,
            4,
            new Mpa(1, "name", "description"),
            new ArrayList<Integer>(),
            new ArrayList<Genre>());

    private final Film film2 = new Film(2,
            "filmname2",
            "filmdescription12",
            LocalDate.now().minusYears(20),
            100,
            4,
            new Mpa(2, "name", "description"),
            new ArrayList<Integer>(),
            new ArrayList<Genre>());

    private final Film film3 = new Film(3,
            "filmname3",
            "filmdescription3",
            LocalDate.now().minusYears(20),
            4,
            100,
            new Mpa(2, "name", "description"),
            new ArrayList<Integer>(),
            new ArrayList<Genre>());

    private final Film film4 = new Film(4,
            "filmname4",
            "filmdescription4",
            LocalDate.now().minusYears(20),
            4,
            100,
            new Mpa(2, "name", "description"),
            new ArrayList<Integer>(),
            new ArrayList<Genre>());

    @Test
    public void createTest() {
        filmStorage.create(film1);
        AssertionsForClassTypes.assertThat(film1).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(film1).extracting("name").isNotNull();
    }

    @Test
    public void getByIdTest() {
        filmStorage.create(film1);
        Film dbFilm = filmStorage.getById(1);
        AssertionsForClassTypes.assertThat(dbFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void putTest() {
        Film film = filmStorage.create(film1);
        film.setName("new name");
        filmStorage.put(film);
        Film dbFilm = filmStorage.getById(film.getId());
        AssertionsForClassTypes.assertThat(dbFilm).hasFieldOrPropertyWithValue("name", "new name");
    }

    @Test
    public void findAllAndDeleteByIdTest() {
        Collection<Film> list = filmStorage.findAll();
        for (Film film : list) {
            filmStorage.deleteById(film.getId());
        }
        list = filmStorage.findAll();
        Assertions.assertEquals(list.size(), 0);
        Film dbFilm1 = filmStorage.create(film1);
        Film dbFilm2 = filmStorage.create(film2);
        list = filmStorage.findAll();
        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    public void deleteFilmTest() {
        Film dbFilm1 = filmStorage.create(film1);
        Film dbFilm2 = filmStorage.create(film2);
        Collection<Film> first = filmStorage.findAll();
        filmStorage.deleteById(dbFilm1.getId());
        Collection<Film> second = filmStorage.findAll();
        Assertions.assertEquals(first.size(), second.size() + 1);
    }
}