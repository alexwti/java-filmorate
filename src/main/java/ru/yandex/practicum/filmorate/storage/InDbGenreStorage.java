package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class InDbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        String sqlQuery = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    @Override
    public Genre getById(int id) {
        final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Жанр фильма с id: '%d' не найден", id));
        }
    }

    @Override
    public Collection<Genre> getFilmGenres(int filmId) {
        String sqlQuery = "SELECT T1.GENRE_ID, T1.GENRE_NAME FROM GENRES T1 JOIN FILM_GENRE T2 ON T1.GENRE_ID = T2.GENRE_ID " +
                "WHERE T2.FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("GENRE_ID"), resultSet.getString("GENRE_NAME"));
    }
}
