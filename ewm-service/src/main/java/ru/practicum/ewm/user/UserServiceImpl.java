package ru.practicum.ewm.user;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.pageable.OffsetPageRequest;
import ru.practicum.ewm.rating.RatingService;


@Service
@RequiredArgsConstructor
@Logged
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RatingService ratingService;

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        Page<User> users;
        if (ids == null) {
            users = userRepository.findAll(new OffsetPageRequest(from, size, Sort.by("id")));
        } else {
            users = userRepository.findAllByIdIn(ids, new OffsetPageRequest(from, size));
        }
        List<Integer> userIds = users.map(User::getId).getContent();
        Map<Integer, Float> ratingsByUserIds = ratingService.getLikesAndTotalForUser(userIds);
        return users.map(user -> userMapper.toUserDtoForAdmin(user,
                ratingsByUserIds.getOrDefault(user.getId(), 0.0f))).getContent();
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = userRepository.save(userMapper.toUser(userDto));
        return userMapper.toUserDtoForAdminWithoutRating(user);
    }

    @Override
    @Transactional
    public User deleteUser(int userId) {
        User user = userRepository.findByIdOrThrow(userId);
        userRepository.deleteById(userId);
        return user;
    }
}
