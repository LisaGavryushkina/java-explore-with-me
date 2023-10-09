package ru.practicum.ewm.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.event.exception.EventNotFoundException;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.request.exception.EventNotPublishedException;
import ru.practicum.ewm.request.exception.ParticipantLimitExceededException;
import ru.practicum.ewm.request.exception.RepeatedRequestException;
import ru.practicum.ewm.request.exception.RequestFromInitiatorException;
import ru.practicum.ewm.request.exception.RequestNotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserNotFoundException;
import ru.practicum.ewm.user.UserRepository;

@Service
@RequiredArgsConstructor
@Logged
@Transactional
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<RequestDto> getRequests(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return requests.stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto addRequest(int userId, int eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        boolean isExist = requestRepository.existsByRequesterIdAndEventId(userId, eventId);
        if (isExist) {
            throw new RepeatedRequestException(userId, eventId);
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getInitiator().getId() == userId) {
            throw new RequestFromInitiatorException(userId, eventId);
        }
        if (event.getState() != State.PUBLISHED) {
            throw new EventNotPublishedException(eventId);
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit() &&
                event.getParticipantLimit() != 0) {
            throw new ParticipantLimitExceededException(eventId);
        }
        Request request;
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request = requestRepository.save(requestMapper.toRequest(user, event, LocalDateTime.now(),
                    Status.CONFIRMED));
        } else {
            request = requestRepository.save(requestMapper.toRequest(user, event, LocalDateTime.now(),
                    Status.PENDING));
        }
        return requestMapper.toRequestDto(request);
    }

    @Override
    public RequestDto cancelRequest(int userId, int requestId) {
        Request request =
                requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
        request.setStatus(Status.CANCELED);
        Request updated = requestRepository.save(request);
        return requestMapper.toRequestDto(updated);
    }
}
