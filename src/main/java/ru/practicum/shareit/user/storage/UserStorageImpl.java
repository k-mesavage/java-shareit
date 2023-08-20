package ru.practicum.shareit.user.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UpdateUserBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class UserStorageImpl implements UserStorage {

    private static Map<Long, User> users = new HashMap<>();
    private static Long id = 0L;
    private final UserMapper mapper;

    @Override
    public User addUser(UserDto userDto) {
        if (emailChecker(userDto.getEmail())) {
            throw new RuntimeException("Invalid Email Exception");
        }
        User newUser = mapper.fromUserDto(userDto);
        newUser.setId(generateId());
        users.put(newUser.getId(), newUser);
        return getUserById(newUser.getId());
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
    public User updateUser(String params, Long userId) {
        User updatedUser = getUserById(userId);
        if (updatedUser == null) {
            throw new ObjectNotFoundException("User Not Found");
        }
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        UpdateUserBuilder updateUserBuilder;
        try {
            updateUserBuilder = objectMapper.readValue(params, UpdateUserBuilder.class);
            updateUserBuilder.id = userId;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Invalid Format", e);
        }
        if (updateUserBuilder.name != null) {
            updatedUser.setName(updateUserBuilder.name);
        }
        if (updateUserBuilder.email != null) {
            if (!updateUserBuilder.email.equals(updatedUser.getEmail())) {
                emailChecker(updateUserBuilder.email);
                updatedUser.setEmail(updateUserBuilder.email);
            }
            updatedUser.setEmail(updateUserBuilder.email);
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
