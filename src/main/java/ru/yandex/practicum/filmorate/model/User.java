package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NotNull(message = "EMail должен быть заполнен")
    @Email(message = "EMail не соответствует шаблону")
    private final String email;
    @NotNull(message = "Логин должен быть не пустым")
    @NotBlank(message = "Логин должен быть не пустым")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Логин содержит недопустимые символы")
    private final String login;
    private String name;
    @NotNull(message = "Дата рождения должна быть заполнена")
    @Past(message = "Дата рождения не может превышать текущую дату")
    private final LocalDate birthday;
}
