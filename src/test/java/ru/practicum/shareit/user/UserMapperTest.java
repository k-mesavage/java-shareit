package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    private final User user = new User(1L, "User name", "user@email.com");
    private final UserDto userDto = UserDto.builder().id(1L).name("User name").email("user@email.com").build();

    @Test
    void toUserDto() {
        UserDto expectedUser = userMapper.toUserDto(user);
        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getName(), user.getName());
        assertEquals(expectedUser.getEmail(), user.getEmail());
    }

    @Test
    void fromUserDto() {
        User expectedUser = userMapper.fromUserDto(userDto);
        assertEquals(expectedUser.getId(), userDto.getId());
        assertEquals(expectedUser.getName(), userDto.getName());
        assertEquals(expectedUser.getEmail(), userDto.getEmail());
    }

    @Test
    void toBooking() {
        ShortUserDto expectedUser = userMapper.toBooking(user);
        assertEquals(expectedUser.getId(), user.getId());
    }
}