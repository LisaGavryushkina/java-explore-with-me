package ru.practicum.ewm.user;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.pageable.OffsetPageRequest;

import static ru.practicum.ewm.user.UserRepository.LikesAndTotal;

@Service
@RequiredArgsConstructor
@Logged
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        Page<User> users;
        if (ids == null) {
            users = userRepository.findAll(new OffsetPageRequest(from, size, Sort.by("id")));
        } else {
            users = userRepository.findAllByIdIn(ids, new OffsetPageRequest(from, size));
        }
        Map<Integer, LikesAndTotal> likesAndTotalByUserIds = users.stream()
                .collect(Collectors.toMap(User::getId,
                        user -> userRepository.countLikesAndTotalForInitiator(user.getId())));
        return users.map(user -> userMapper.toUserDtoForAdmin(user, likesAndTotalByUserIds.get(user.getId()))).getContent();
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = userRepository.save(userMapper.toUser(userDto));
        return userMapper.toUserDtoForAdmin(user);
    }

    @Override
    @Transactional
    public User deleteUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.deleteById(userId);
        return user;
    }
}
