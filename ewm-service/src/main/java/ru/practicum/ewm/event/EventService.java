package ru.practicum.ewm.event;

import java.util.List;

import ru.practicum.ewm.request.RequestDto;

public interface EventService {

    List<EventShortedForResponseDto> getInitiatorEvents(int userId, int from, int size);

    EventForResponseDto addEvent(int userId, EventForRequestDto eventForRequestDto);

    EventForResponseDto getEvent(int userId, int eventId);

    EventForResponseDto updateEventByInitiator(int userId, int eventId, EventForRequestDto eventForRequestDto);

    List<RequestDto> getRequestsForInitiatorEvent(int userId, int eventId);

    UpdatedRequestsStatusDto updateRequestsStatusByInitiator(int userId, int eventId,
                                                             ToUpdateRequestsStatusDto toUpdateRequestsStatusDto);
}
