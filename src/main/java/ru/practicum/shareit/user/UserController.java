package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    UserService service;

    @PostMapping
    public User addUser(@RequestBody @Valid UserDto userDto) {
        log.info("Начало обработки запроса на добавление пользователя");
        User user = service.addUser(userDto);
        log.info("Окончание обработки запроса на добавление пользователя");
        return user;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Long userId) {
        log.info("Начало обработки запроса на получения пользователя {}", userId);
        User user = service.getUserById(userId);
        log.info("Окончание обработки запроса на получения пользователя {}", userId);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Начало обработки запроса на получение всех пользователей");
        List<User> users = service.getAllUsers();
        log.info("Окончание обработки запроса на получение всех пользователей");
        return users;
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable Long userId,
                           @RequestBody String params) {
        log.info("Начало обработки запроса на обновление пользователя {}", userId);
        User user = service.updateUser(params, userId);
        log.info("Окончание обработки запроса на обновление пользователя {}", userId);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Начало обработки запроса на удаление пользователя {}", userId);
        service.deleteUser(userId);
        log.info("Окончание обработки запроса на удаление пользователя {}", userId);
    }
}
