package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

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
        return Optional.ofNullable(storage.getUserById(id))
                .orElseThrow(() -> new ObjectNotFoundException("User"));
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
