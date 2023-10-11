package ru.practicum.ewm.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.dto.UpdatedRequestsStatusDto;
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

    public UpdatedRequestsStatusDto toUpdatedRequestsStatusDto(List<Request> confirmedRequests,
                                                               List<Request> rejectedRequests) {
        return new UpdatedRequestsStatusDto(confirmedRequests.stream()
                .map(this::toRequestDto)
                .collect(Collectors.toList()),
                rejectedRequests.stream()
                        .map(this::toRequestDto)
                        .collect(Collectors.toList()));
    }
}
