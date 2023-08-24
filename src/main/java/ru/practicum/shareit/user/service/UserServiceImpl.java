package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    @Override
    public User addUser(User user) {
        try {
            return storage.addUser(user);
        } catch (NullPointerException e) {
            throw new BadRequestException("Bad Request Exception");
        }
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
