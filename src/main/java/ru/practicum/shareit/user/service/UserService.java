package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User addUser(UserDto user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(String params, Long userId);

    void deleteUser(Long id);
}
