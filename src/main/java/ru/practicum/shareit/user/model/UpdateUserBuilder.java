package ru.practicum.shareit.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserBuilder {
    @JsonProperty("id")
    public Long id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("email")
    public String email;
}
