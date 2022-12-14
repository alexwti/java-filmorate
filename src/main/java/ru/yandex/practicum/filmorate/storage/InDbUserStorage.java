package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InDbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        final String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "VALUES ( ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement prepareStatement = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            prepareStatement.setString(1, user.getEmail());
            prepareStatement.setString(2, user.getLogin());
            prepareStatement.setString(3, user.getName());
            prepareStatement.setDate(4, Date.valueOf(user.getBirthday()));
            return prepareStatement;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User put(User user) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        try {
            jdbcTemplate.queryForObject(sqlQuery, this::makeUser, user.getId());
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", user.getId()));
        }
        final String sqlQueryUpd = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQueryUpd, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getById(user.getId());
    }

    @Override
    public User getById(int id) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        User user;
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        }
    }

    @Override
    public User deleteById(int id) {
        final String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        User user = getById(id);

        jdbcTemplate.update(sqlQuery, id);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        final String sqlQuery = "SELECT * FROM USERS";

        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public List<User> addFriend(int fromId, int toId) {
        final String sqlStringCheck = "SELECT * FROM FRIENDSHIP WHERE USER_FROM = ? AND USER_TO = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlStringCheck, fromId, toId);
        if (rowSet.first()) {
            final String sqlString = "UPDATE FRIENDSHIP SET STATUS_REL_ID = ? WHERE USER_FROM = ? AND USER_TO = ?";
            jdbcTemplate.update(sqlString, 2, fromId, toId);
        } else {
            final String sqlString = "INSERT INTO FRIENDSHIP (USER_FROM, USER_TO, STATUS_REL_ID) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlString, fromId, toId, 2);
        }
        return List.of(getById(fromId), getById(toId));
    }

    @Override
    public List<User> deleteFriend(int fromId, int toId) {
        List<User> users = List.of(getById(fromId), getById(toId));
        final String sqlString = "DELETE FROM FRIENDSHIP WHERE USER_FROM = ? AND USER_TO = ?";
        jdbcTemplate.update(sqlString, fromId, toId);
        return users;
    }

    @Override
    public List<User> getFriendsById(int id) {
        final String sqlQuery = "SELECT U.* FROM USERS U, FRIENDSHIP F WHERE F.USER_TO = U.USER_ID" +
                " AND F.STATUS_REL_ID = 2 AND F.USER_FROM = ?";
        return jdbcTemplate.query(sqlQuery, this::makeUser, id);
    }

    @Override
    public List<User> getCommonFriendsList(int firstUser, int secondUser) {
        final String sqlQuery = "SELECT * FROM (" +
                "SELECT T1.* FROM USERS T1, FRIENDSHIP F WHERE F.USER_FROM = T1.USER_ID AND F.USER_TO = ? AND F.STATUS_REL_ID = 2 " +
                "UNION " +
                "SELECT T1.* FROM USERS T1, FRIENDSHIP F WHERE F.USER_TO = T1.USER_ID AND F.USER_FROM = ? AND F.STATUS_REL_ID = 2 " +
                ") s " +
                "WHERE S.USER_ID IN " +
                "(" +
                "    SELECT T1.USER_ID FROM USERS T1, FRIENDSHIP F WHERE F.USER_FROM = T1.USER_ID AND F.USER_TO = ? AND F.STATUS_REL_ID = 2 " +
                "    UNION " +
                "    SELECT T1.USER_ID FROM USERS T1, FRIENDSHIP F WHERE F.USER_TO = T1.USER_ID AND F.USER_FROM = ? AND F.STATUS_REL_ID = 2 " +
                ")";
        return jdbcTemplate.query(sqlQuery, this::makeUser, secondUser, secondUser, firstUser, firstUser);
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        int userId = resultSet.getInt("USER_ID");
        return new User(
                userId,
                resultSet.getString("EMAIL"),
                resultSet.getString("LOGIN"),
                resultSet.getString("USER_NAME"),
                resultSet.getDate("BIRTHDAY").toLocalDate(),
                getFriendsById(userId));
    }
}
