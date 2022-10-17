package ru.practicum.service.user.dto;

import ru.practicum.service.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserMapper {
    public static User toUser(UserCreateDto userCreateDto) {
        return new User(null, userCreateDto.getEmail(), userCreateDto.getName(), true);
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static List<UserDto> mapToAllUserDto(Collection<User> users) {
        List<UserDto> usersDtoList = new ArrayList<>();
        for (User user : users) {
            usersDtoList.add(toUserDto(user));
        }
        return usersDtoList;
    }
}