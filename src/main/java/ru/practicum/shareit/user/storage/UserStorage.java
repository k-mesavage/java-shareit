package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(User user, Long userId);

    void deleteUser(Long id);
}
