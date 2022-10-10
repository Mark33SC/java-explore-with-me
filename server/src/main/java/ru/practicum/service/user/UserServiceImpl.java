package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.exception.user.UserNotFoundException;
import ru.practicum.service.user.dto.UserCreateDto;
import ru.practicum.service.user.dto.UserDto;
import ru.practicum.service.user.dto.UserMapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> search(Long[] ids, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        List<Long> id = Arrays.asList(ids);
        Collection<User> result = userRepository.findAllById(id, pageRequest);
        return UserMapper.mapToAllUserDto(result);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(
                        String.format("User with id:%s not found", userId)
                )
        );
    }

    @Override
    @Transactional
    public UserDto addUser(UserCreateDto userCreateDto) {
        User user = UserMapper.toUser(userCreateDto);
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto deleteUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id:%s not found", userId))
        );
        userRepository.deleteById(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto activateById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id:%s not found", userId))
        );

        user.setActivated(true);

        return UserMapper.toUserDto(user);
    }
}
