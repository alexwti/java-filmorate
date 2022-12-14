package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class InDbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;
    private final MpaService mpaService;


    @Override
    public Film create(Film film) {
        final String sqlQuery = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                addGenre(film.getId(), genre);
            }
        }

        if (film.getLikesUserID() != null) {
            for (Integer likesUserID : film.getLikesUserID()) {
                addLike(film.getId(), likesUserID);
            }
        }
        return film;
    }

    @Override
    public Film put(Film film) {
        final String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, film.getId());
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Фильм с id %d не найден", film.getId()));
        }

        String sqlQueryUpd = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryUpd,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        final String sqlQueryGenre = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryGenre, film.getId());

        final String sqlQueryLike = "DELETE FROM FILM_LIKES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryGenre, film.getId());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                addGenre(film.getId(), genre);
            }
        }

        if (film.getLikesUserID() != null) {
            for (Integer likesUserID : film.getLikesUserID()) {
                addLike(film.getId(), likesUserID);
            }
        }

        return getById(film.getId());
    }

    @Override
    public Collection<Film> findAll() {
        final String sqlQuery = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Film deleteById(int id) {
        Film film = getById(id);
        final String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return film;
    }

    @Override
    public Film getById(int id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Фильм с id %d не найден", id));
        }
    }

    @Override
    public Film addLike(int filmId, int userId) {
        final String sqlQuery = "INSERT INTO FILM_LIKES (USER_ID, FILM_ID) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery, userId, filmId);
        return getById(filmId);
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        final String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return getById(filmId);
    }

    @Override
    public List<Film> getTopRaitingFilms(int count) {
        final String sqlQuery = "SELECT * FROM FILMS ORDER BY RATE DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);
    }

    @Override
    public Film addGenre(int filmId, Genre genre) {
        final String sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, filmId, genre.getId());
            return getById(filmId);
        } catch (DuplicateKeyException exception) {
            return null;
        }
    }

    @Override
    public Film removeGenre(int filmId, Genre genre) {
        final String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ? AND FILM_GENRE_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, genre.getId());
        return getById(filmId);
    }

    @Override
    public List<Integer> getLikes(Integer filmId) {
        String sqlQuery = "SELECT USER_ID FROM FILM_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
    }

    private Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int filmId = resultSet.getInt("FILM_ID");
        Film film = new Film(
                filmId,
                resultSet.getString("FILM_NAME"),
                resultSet.getString("DESCRIPTION"),
                Objects.requireNonNull(resultSet.getDate("RELEASE_DATE")).toLocalDate(),
                resultSet.getInt("DURATION"),
                resultSet.getInt("RATE"),
                mpaService.getById(resultSet.getInt("RATING_ID")),
                getLikes(filmId),
                (List<Genre>) genreService.getFilmGenres(filmId)
        );
        return film;
    }
}
