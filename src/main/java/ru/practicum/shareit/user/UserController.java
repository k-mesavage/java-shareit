package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.CreateConstraint;
import ru.practicum.shareit.validation.UpdateConstraint;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UserDto addUser(@RequestBody @Validated(CreateConstraint.class) UserDto userDto) {
        log.info("Начало обработки запроса на добавление пользователя");
        UserDto user = service.addUser(userDto);
        log.info("Окончание обработки запроса на добавление пользователя");
        return user;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") Long userId) {
        log.info("Начало обработки запроса на получения пользователя {}", userId);
        UserDto user = service.getUserById(userId);
        log.info("Окончание обработки запроса на получения пользователя {}", userId);
        return user;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Начало обработки запроса на получение всех пользователей");
        List<UserDto> users = service.getAllUsers();
        log.info("Окончание обработки запроса на получение всех пользователей");
        return users;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId,
                              @RequestBody @Validated(UpdateConstraint.class) UserDto userDto) {
        log.info("Начало обработки запроса на обновление пользователя {}", userId);
        UserDto user = service.updateUser(userDto, userId);
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
