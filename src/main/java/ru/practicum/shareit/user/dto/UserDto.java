package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.CreateConstraint;
import ru.practicum.shareit.validation.UpdateConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDto {

    private Long id;

    @NotEmpty(groups = CreateConstraint.class)
    private String name;

    @NotEmpty(groups = CreateConstraint.class)
    @Email(groups = {CreateConstraint.class, UpdateConstraint.class})
    private String email;

}
