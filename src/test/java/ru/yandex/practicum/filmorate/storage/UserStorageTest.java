package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class UserStorageTest {
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

    public void createTest() {
        User dbUser = userStorage.create(user1);
        AssertionsForClassTypes.assertThat(dbUser).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(dbUser).extracting("name").isNotNull();
    }

    @Test
    public void putTest() {
        User user = userStorage.create(user1);
        user.setName("new name");
        userStorage.put(user);
        User dbUser = userStorage.getById(user.getId());
        AssertionsForClassTypes.assertThat(dbUser).hasFieldOrPropertyWithValue("name", "new name");
    }

    @Test
    public void findAllAndDeleteByIdTest() {
        Collection<User> list = userStorage.findAll();
        for (User user : list) {
            List<User> friend = userStorage.getFriendsById(user.getId());
            for (User dbUser : friend) {
                userStorage.deleteFriend(user.getId(), dbUser.getId());
            }
            userStorage.deleteById(user.getId());
        }
        list = userStorage.findAll();
        assertEquals(list.size(), 0);
        userStorage.create(user2);
        userStorage.create(user3);
        list = userStorage.findAll();
        assertEquals(list.size(), 2);
    }

    @Test
    public void addFriendAndGetFriendsByIdAndDeleteFriendTest() {
        User userFrom = userStorage.create(user1);
        User userTo = userStorage.create(user2);
        userStorage.addFriend(userFrom.getId(), userTo.getId());
        userStorage.addFriend(userFrom.getId(), userTo.getId());
        List<User> list = userStorage.getFriendsById(userFrom.getId());
        assertEquals(list.get(0).getId(), userTo.getId());
        userStorage.deleteFriend(userFrom.getId(), userTo.getId());
        list = userStorage.getFriendsById(userFrom.getId());
        assertEquals(list.size(), 0);
    }

    @Test
    public void getCommonFriendsListTest() {
        User dbUser1 = userStorage.create(user1);
        User dbUser2 = userStorage.create(user2);
        User dbUser3 = userStorage.create(user3);
        User dbUser4 = userStorage.create(user4);
        User dbUser5 = userStorage.create(user5);
        User dbUser6 = userStorage.create(user6);

        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user1.getId(), user2.getId()); //При повторном дорбавлении меняется статус на подтвержденный
        userStorage.addFriend(user1.getId(), user4.getId());
        userStorage.addFriend(user1.getId(), user4.getId());
        userStorage.addFriend(user1.getId(), user5.getId());
        userStorage.addFriend(user1.getId(), user5.getId());
        userStorage.addFriend(user1.getId(), user6.getId());
        userStorage.addFriend(user1.getId(), user6.getId());

        userStorage.addFriend(user2.getId(), user3.getId());
        userStorage.addFriend(user2.getId(), user3.getId());
        userStorage.addFriend(user2.getId(), user4.getId());
        userStorage.addFriend(user2.getId(), user4.getId());
        userStorage.addFriend(user2.getId(), user6.getId());
        userStorage.addFriend(user2.getId(), user6.getId());

        List list = userStorage.getCommonFriendsList(user1.getId(), user2.getId());
        assertEquals(list.size(), 2);
    }

}