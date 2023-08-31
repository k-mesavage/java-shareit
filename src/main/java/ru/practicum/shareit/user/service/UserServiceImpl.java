package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    @Override
    @Transactional
    public User addUser(User user) {
        return storage.save(user);
    }

    @Override
    @Transactional
    public User getUserById(Long id) {
        return storage.getReferenceById(id);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return storage.findAll();
    }

    @Override
    @Transactional
    public User updateUser(User user, Long userId) {
        User updatedUser = getUserById(userId);
        if (user.getName() != null && !user.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        storage.deleteById(id);
    }
}
