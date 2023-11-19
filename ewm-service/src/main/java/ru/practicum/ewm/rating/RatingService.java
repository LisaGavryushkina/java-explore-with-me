package ru.practicum.ewm.rating;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.user.UserRepository;


@Service
@RequiredArgsConstructor
public class RatingService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    public float getLikesAndTotalForUser(int userId) {
        List<LikesAndTotal> likesAndTotal = userRepository.countLikesAndTotalForUsers(List.of(userId));
        if (likesAndTotal.isEmpty()) {
            return 0.0f;
        }
        return getRating(likesAndTotal.get(0));
    }

    public float getLikesAndTotalForEvent(int eventId) {
        List<LikesAndTotal> likesAndTotal = eventRepository.countLikesAndTotalForEvents(List.of(eventId));
        if (likesAndTotal.isEmpty()) {
            return 0.0f;
        }
        return getRating(likesAndTotal.get(0));
    }

    public Map<Integer, Float> getLikesAndTotalForUser(Collection<Event> events) {
        Set<Integer> userIds = events.stream()
                .map(event -> event.getInitiator().getId())
                .collect(Collectors.toSet());
        return userRepository.countLikesAndTotalForUsers(userIds).stream()
                .collect(Collectors.toMap(LikesAndTotal::getEntityId, this::getRating));
    }

    public Map<Integer, Float> getLikesAndTotalForUser(List<Integer> userIds) {
        Set<Integer> uniqueIds = new HashSet<>(userIds);
        return userRepository.countLikesAndTotalForUsers(uniqueIds).stream()
                .collect(Collectors.toMap(LikesAndTotal::getEntityId, this::getRating));

    }

    public Map<Integer, Float> getLikesAndTotalForEvent(Collection<Event> events) {
        Set<Integer> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        return eventRepository.countLikesAndTotalForEvents(eventIds).stream()
                .collect(Collectors.toMap(LikesAndTotal::getEntityId, this::getRating));
    }

    private float getRating(LikesAndTotal likesAndTotal) {
        int likes = likesAndTotal.getLikes();
        int total = likesAndTotal.getTotal();
        if (likes == 0) {
            return 0.0f;
        }
        float rating = (float) likes / total * 10;
        return Float.parseFloat(String.format("%.1f", rating));
    }
}
