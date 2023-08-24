package ru.practicum.shareit.user.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class UserStorageImpl implements UserStorage {

    private static final Map<Long, User> users = new HashMap<>();
    private static Long id = 0L;

    @Override
    public User addUser(User user) {
        if (emailChecker(user.getEmail())) {
            throw new RuntimeException("Invalid Email Exception");
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user, Long userId) {
        User updatedUser = getUserById(userId);
        if (updatedUser == null) {
            throw new ObjectNotFoundException("User Not Found");
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(updatedUser.getEmail())) {
                emailChecker(user.getEmail());
                updatedUser.setEmail(user.getEmail());
            }
        }
        users.put(updatedUser.getId(), updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    private Long generateId() {
        return ++id;
    }

    private boolean emailChecker(String email) {
        for (User user : getAllUsers()) {
            if (email.equals(user.getEmail())) {
                throw new RuntimeException("Invalid Email Exception");
            }
        }
        return false;
    }
}
