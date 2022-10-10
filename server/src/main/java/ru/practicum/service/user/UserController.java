package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.user.dto.UserCreateDto;
import ru.practicum.service.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> search(
            @RequestParam("ids") Long[] userIds,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return userService.search(userIds, from, size);
    }

    @PostMapping
    public UserDto add(@Valid @RequestBody UserCreateDto userCreateDto) {
        return userService.addUser(userCreateDto);
    }

    @DeleteMapping("/{userId}")
    public UserDto delete(@PathVariable long userId) {
        return userService.deleteUserById(userId);
    }

    @PatchMapping("/{userId}/activate")
    public UserDto activate(@PathVariable long userId) {
        return userService.activateById(userId);
    }
}
