package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.UpdateConstraint;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        log.info("Начало обработки запроса на добавление пользователя");
        User user = service.addUser(mapper.fromUserDto(userDto));
        log.info("Окончание обработки запроса на добавление пользователя");
        return mapper.toUserDto(user);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") Long userId) {
        log.info("Начало обработки запроса на получения пользователя {}", userId);
        User user = service.getUserById(userId);
        log.info("Окончание обработки запроса на получения пользователя {}", userId);
        return mapper.toUserDto(user);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Начало обработки запроса на получение всех пользователей");
        List<User> users = service.getAllUsers();
        log.info("Окончание обработки запроса на получение всех пользователей");
        return users.stream().map(mapper::toUserDto).collect(Collectors.toList());
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId,
                              @RequestBody @Validated(UpdateConstraint.class) UserDto userDto) {
        log.info("Начало обработки запроса на обновление пользователя {}", userId);
        User user = service.updateUser(mapper.fromUserDto(userDto), userId);
        log.info("Окончание обработки запроса на обновление пользователя {}", userId);
        return mapper.toUserDto(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Начало обработки запроса на удаление пользователя {}", userId);
        service.deleteUser(userId);
        log.info("Окончание обработки запроса на удаление пользователя {}", userId);
    }
}
