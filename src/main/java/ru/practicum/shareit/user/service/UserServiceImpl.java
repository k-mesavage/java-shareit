package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    @Override
    public User addUser(User user) {
        return storage.addUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return storage.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public User updateUser(User user, Long userId) {
        return storage.updateUser(user, userId);
    }

    @Override
    public void deleteUser(Long id) {
        storage.deleteUser(id);
    }
}
