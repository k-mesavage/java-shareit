package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(User user, Long userId);

    void deleteUser(Long id);
}
