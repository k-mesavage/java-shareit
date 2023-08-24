package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.CreateConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDto {

    private Long id;

    @NotEmpty(groups = CreateConstraint.class)
    private String name;

    @Email
    @NotEmpty(groups = CreateConstraint.class)
    private String email;

}
