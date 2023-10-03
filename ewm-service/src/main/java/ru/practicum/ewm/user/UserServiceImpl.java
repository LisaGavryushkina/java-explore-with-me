package ru.practicum.ewm.user;

import java.util.List;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.pageable.OffsetPageRequest;

@Service
@RequiredArgsConstructor
@Logged
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        Page<User> users;
        if (ids == null) {
            users = userRepository.findAll(new OffsetPageRequest(from, size, Sort.unsorted()));
        } else {
            users = userRepository.findAllByIdIn(ids, new OffsetPageRequest(from, size, Sort.unsorted()));
        }
        return users.map(userMapper::toUserDtoForAdmin).getContent();
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userRepository.save(userMapper.toUser(userDto));
        return userMapper.toUserDtoForAdmin(user);
    }

    @Override
    public User deleteUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.deleteById(userId);
        return user;
    }
}
