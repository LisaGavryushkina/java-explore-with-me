package ru.practicum.ewm.user;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;

import static ru.practicum.ewm.user.UserRepository.LikesAndTotal;

@Service
@RequiredArgsConstructor
public class UserRatingService {
    private final UserRepository userRepository;


    public LikesAndTotal getLikesAndTotal(int userId) {
        return userRepository.countLikesAndTotalForInitiator(userId);
    }

    public Map<Integer, LikesAndTotal> getLikesAndTotal(Collection<Event> events) {
        Set<Integer> userIds = events.stream()
                .map(event -> event.getInitiator().getId())
                .collect(Collectors.toSet());
        return userIds.stream()
                .collect(Collectors.toMap(id -> id,
                        userRepository::countLikesAndTotalForInitiator));
    }
}
