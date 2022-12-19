package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Arrays;
import java.util.Collection;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaStorageTest {
    private final MpaStorage mpaStorage;

    @Test
    public void findAllTest() {
        Collection<Mpa> mpa = mpaStorage.findAll();
        Assertions.assertThat(mpa)
                .extracting(Mpa::getName)
                .containsAll(Arrays.asList("G", "PG", "PG-13", "R", "NC-17"));
    }

    @Test
    public void getByIdTest() {
        Mpa mpa = mpaStorage.getById(3);
        AssertionsForClassTypes.assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG-13");
    }
}