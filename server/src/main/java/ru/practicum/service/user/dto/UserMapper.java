package ru.practicum.service.user.dto;

import ru.practicum.service.user.User;

public class UserMapper {
    public static User toUser(UserCreateDto userCreateDto) {
        return new User(null, userCreateDto.getEmail(), userCreateDto.getName(), true);
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }
}