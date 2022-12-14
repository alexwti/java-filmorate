package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @NotNull(message = "EMail должен быть заполнен")
    @Email(message = "EMail не соответствует шаблону")
    private String email;
    @NotNull(message = "Логин должен быть не пустым")
    @NotBlank(message = "Логин должен быть не пустым")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Логин содержит недопустимые символы")
    private String login;
    private String name;
    @NotNull(message = "Дата рождения должна быть заполнена")
    @Past(message = "Дата рождения не может превышать текущую дату")
    private LocalDate birthday;

    @JsonIgnore
    List<User> friends = new ArrayList<>();

    public User(String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
}
