package ru.practicum.service.user;

import ru.practicum.service.user.dto.UserCreateDto;
import ru.practicum.service.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> search(Long[] ids, int from, int size);

    User getUserById(long userId);

    UserDto addUser(UserCreateDto userCreateDto);

    UserDto deleteUserById(long userId);

    UserDto activateById(long userId);

}