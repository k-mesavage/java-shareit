package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.HttpHeader;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;

    private static final String HEADER = HttpHeader.X_SHARER_USER_ID;
    private final ShortUserDto shortUserDto1 = new ShortUserDto(1L);
    private final User user1 = new User(1L, "Joe", "joe@email.com");
    private final ShortItemDto shortItemDto = ShortItemDto.builder()
            .id(1L)
            .name("Bike wheel")
            .build();

    @Nested
    class createTests {
        @Test
        @SneakyThrows
        void shouldCreateNewBooking() {
            LocalDateTime start = LocalDateTime.now().plusSeconds(1);
            LocalDateTime end = LocalDateTime.now().plusDays(1);
            WorkingBookingDto request = WorkingBookingDto.builder()
                    .start(start)
                    .end(end)
                    .itemId(1L)
                    .build();
            BookingDto bookingDto = BookingDto.builder()
                    .id(1L)
                    .status("approve")
                    .booker(shortUserDto1)
                    .item(shortItemDto)
                    .build();

            when(bookingService.addBooking(anyLong(), any()))
                    .thenReturn(bookingDto);

            mvc.perform(post("/bookings")
                            .content(mapper.writeValueAsString(request))
                            .header(HEADER, 1L)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                    .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().id), Long.class))
                    .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                    .andExpect(jsonPath("$.status", is(bookingDto.getStatus())));
        }

        @Test
        @SneakyThrows
        void shouldCreateNewBookingWhenNotFound() {
            LocalDateTime start = LocalDateTime.now().plusSeconds(1);
            LocalDateTime end = LocalDateTime.now().plusDays(1);
            WorkingBookingDto request = WorkingBookingDto.builder()
                    .start(start)
                    .end(end)
                    .itemId(1L)
                    .build();

            when(bookingService.addBooking(anyLong(), any()))
                    .thenThrow(new ObjectNotFoundException("Some object"));

            mvc.perform(post("/bookings")
                            .content(mapper.writeValueAsString(request))
                            .header(HEADER, 1L)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void shouldCreateNewBookingWhenUnsupportedStatus() {
            LocalDateTime start = LocalDateTime.now().plusSeconds(1);
            LocalDateTime end = LocalDateTime.now().plusDays(1);
            WorkingBookingDto request = WorkingBookingDto.builder()
                    .start(start)
                    .end(end)
                    .itemId(1L)
                    .build();

            when(bookingService.addBooking(anyLong(), any()))
                    .thenThrow(new UnsupportedStatusException("Unsupported status"));

            mvc.perform(post("/bookings")
                            .content(mapper.writeValueAsString(request))
                            .header(HEADER, 1L)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class approveTests {
        @Test
        @SneakyThrows
        void shouldApproveBooking() {
            BookingDto bookingDto = BookingDto.builder()
                    .id(1L)
                    .status("approve")
                    .booker(shortUserDto1)
                    .item(shortItemDto)
                    .build();

            when(bookingService.requestBooking(anyBoolean(), anyLong(), anyLong()))
                    .thenReturn(bookingDto);

            mvc.perform(patch("/bookings/{bookingId}", bookingDto.getId())
                            .header(HEADER, 1L)
                            .param("approved", "true")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                    .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().id), Long.class))
                    .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                    .andExpect(jsonPath("$.status", is("approve")));
        }

        @Test
        @SneakyThrows
        void shouldReApproveBooking() {
            LocalDateTime start = LocalDateTime.now().plusSeconds(1);
            LocalDateTime end = LocalDateTime.now().plusDays(1);
            BookingDto bookingDto = BookingDto.builder()
                    .id(1L)
                    .start(start)
                    .end(end)
                    .status("approve")
                    .booker(shortUserDto1)
                    .item(shortItemDto)
                    .build();
            when(bookingService.requestBooking(anyBoolean(), anyLong(), anyLong()))
                    .thenThrow(new BadRequestException("ReApproved Exception"));

            mvc.perform(patch("/bookings/{bookingId}", bookingDto.getId())
                            .header(HEADER, 1L)
                            .param("approved", "true")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class getTest {
        @Test
        @SneakyThrows
        void shouldGetBookingById() {
            LocalDateTime start = LocalDateTime.now().plusSeconds(1);
            LocalDateTime end = LocalDateTime.now().plusDays(1);
            BookingDto bookingDto = BookingDto.builder()
                    .id(1L)
                    .start(start)
                    .end(end)
                    .status("approve")
                    .booker(shortUserDto1)
                    .item(shortItemDto)
                    .build();

            when(bookingService.getBookingById(anyLong(), anyLong()))
                    .thenReturn(bookingDto);

            mvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                            .header(HEADER, 1L)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                    .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().id), Long.class))
                    .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                    .andExpect(jsonPath("$.status", is("approve")));
        }

        @Test
        @SneakyThrows
        void shouldGetAllBookingsByUser() {
            LocalDateTime start = LocalDateTime.now().plusSeconds(1);
            LocalDateTime end = LocalDateTime.now().plusDays(1);
            BookingDto bookingDto = BookingDto.builder()
                    .id(1L)
                    .start(start)
                    .end(end)
                    .status("approve")
                    .booker(shortUserDto1)
                    .item(shortItemDto)
                    .build();

            when(bookingService.getAllBookingsByUser(any(), anyLong(), anyString(), anyInt(), anyInt()))
                    .thenReturn(List.of(bookingDto));

            mvc.perform(
                            get("/bookings")
                                    .header("X-Sharer-User-Id", user1.getId())
                                    .param("state", "ALL")
                                    .param("from", "1")
                                    .param("size", "1")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                    .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                    .andExpect(jsonPath("$.[0].item.name", is(bookingDto.getItem().getName())))
                    .andExpect(jsonPath("$.[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                    .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus())));
        }
    }
}
