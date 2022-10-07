package ru.practicum.service.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserCreateDto {

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String name;

}
