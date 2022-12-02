package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.FilmReleaseDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;
    @NotNull(message = "Наименование должно быть заполнено")
    @NotBlank(message = "Наименование должно быть заполнено")
    private String name;
    @Size(min=0, max=200, message = "Описание не должно превышать 200 символов")
    private String description;
    @NotNull(message = "Дата релиза должна быть заполнено")
    @FilmReleaseDate(message = "Некорректная дата релиза фильма")
    private LocalDate releaseDate;
    @NotNull
    @Min(value = 0, message = "Продолжительность фильма не может быть меньше нуля")
    private int duration;

    @JsonIgnore
    private Set<Integer> likesUserID = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}