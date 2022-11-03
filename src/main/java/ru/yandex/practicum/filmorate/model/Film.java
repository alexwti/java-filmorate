package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.FilmReleaseDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private int id;
    @NotNull(message = "Наименование должно быть заполнено")
    @NotBlank(message = "Наименование должно быть заполнено")
    private final String name;
    @Size(min=0, max=200, message = "Описание не должно превышать 200 символов")
    private final String description;
    @NotNull(message = "Дата релиза должна быть заполнено")
    @FilmReleaseDate(message = "Некорректная дата релиза фильма")
    private final LocalDate releaseDate;
    @NotNull
    @Min(value = 0, message = "Продолжительность фильма не может быть меньше нуля")
    private final int duration;
}