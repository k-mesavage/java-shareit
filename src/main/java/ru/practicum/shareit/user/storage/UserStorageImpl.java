package ru.practicum.shareit.user.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailAlreadyException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@AllArgsConstructor
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private static Long id = 0L;

    @Override
    public User addUser(User user) {
        emailChecker(user.getEmail());
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return Optional.ofNullable(users.get(id)).orElseThrow(() -> new ObjectNotFoundException("User"));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user, Long userId) {
        final User updatedUser = getUserById(userId);
        if (user.getName() != null && !user.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!user.getEmail().equals(updatedUser.getEmail())) {
                emailChecker(user.getEmail());
                updatedUser.setEmail(user.getEmail());
            }
        }
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    private Long generateId() {
        return ++id;
    }

    private void emailChecker(String email) {
        for (User user : getAllUsers()) {
            if (email.equals(user.getEmail())) {
                throw new EmailAlreadyException("The email address is already in use.");
            }
        }
    }
}
