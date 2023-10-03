package ru.practicum.shareit.user;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserStorage userStorage;
    @InjectMocks
    private UserServiceImpl userService;

    private final User actualUser = new User(1L, "User name", "user@email.com");
    private final UserDto actualUserDto = UserDto.builder().id(1L).name("User name").email("user@email.com").build();

    @Nested
    class CreateTests {
        @Test
        void addUser() {
            when(userMapper.fromUserDto(any()))
                    .thenReturn(actualUser);
            when(userMapper.toUserDto(any()))
                    .thenReturn(actualUserDto);
            UserDto expectedUser = userService.addUser(actualUserDto);
            assertEquals(expectedUser.getId(), actualUserDto.getId());
            verify(userStorage).save(any());
        }

        @Test
        void updateUser() {
            when(userMapper.fromUserDto(any()))
                    .thenReturn(actualUser);
            when(userService.getUserById(anyLong()))
                    .thenReturn(actualUserDto);
            UserDto expectedUser = userService.updateUser(actualUserDto, 1L);
            assertEquals(expectedUser.getName(), actualUserDto.getName());
            assertEquals(expectedUser.getEmail(), actualUserDto.getEmail());
            verify(userStorage).save(any());
        }
    }

    @Nested
    class GetTest {
        @Test
        void getUserById() {
            when(userMapper.toUserDto(any()))
                    .thenReturn(actualUserDto);

            final UserDto expectedUser = userService.getUserById(1L);
            assertEquals(expectedUser.getId(), actualUserDto.getId());
            verify(userStorage).getReferenceById(anyLong());
        }

        @Test
        void getAllUsers() {
            userService.getAllUsers();
            verify(userStorage).findAll();
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void deleteUser() {
            userService.deleteUser(1L);
            verify(userStorage).deleteById(anyLong());
        }
    }
}