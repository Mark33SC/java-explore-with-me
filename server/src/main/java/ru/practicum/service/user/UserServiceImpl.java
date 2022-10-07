package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.service.exception.user.UserNotFoundException;
import ru.practicum.service.user.dto.UserCreateDto;
import ru.practicum.service.user.dto.UserDto;
import ru.practicum.service.user.dto.UserMapper;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> search(long[] ids, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        Page<User> result = userRepository.findAllByIdIn(ids, pageRequest);

        return result.map(UserMapper::toUserDto).toList();
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(
                        String.format("User with id:%s not found", userId)
                )
        );
    }

    @Override
    public UserDto addUser(UserCreateDto userCreateDto) {
        User user = UserMapper.toUser(userCreateDto);
        userRepository.save(user);
        log.info("User with email: {} added, assigned id:{}", userCreateDto.getEmail(), user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto deleteUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id:%s not found", userId))
        );
        userRepository.deleteById(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto activateById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id:%s not found", userId))
        );

        user.setActivated(true);

        return UserMapper.toUserDto(user);
    }
}
