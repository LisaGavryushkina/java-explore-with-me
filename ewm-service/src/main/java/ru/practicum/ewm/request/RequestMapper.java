package ru.practicum.ewm.request;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.user.User;

@Component
public class RequestMapper {

    public RequestDto toRequestDto(Request request) {
        return new RequestDto(request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public Request toRequest(User requester, Event event, LocalDateTime created, Status status) {
        return new Request(0,
                created,
                event,
                requester,
                status);
    }
}
