package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto addUser(UserDto user) {
        return userMapper.toUserDto(storage.save(userMapper.fromUserDto(user)));
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        return userMapper.toUserDto(storage.getReferenceById(id));
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        return storage.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto user, Long userId) {
        UserDto updatedUser = getUserById(userId);
        if (user.getName() != null && !user.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }
        storage.save(userMapper.fromUserDto(updatedUser));
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        storage.deleteById(id);
    }
}
