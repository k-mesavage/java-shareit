package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    private static final String HEADER = "X-Sharer-User-Id";
    private final UserDto user = UserDto.builder().id(1L).name("User").email("user@email.com").build();


    @Test
    void addUser() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}