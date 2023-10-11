package ru.practicum.ewm.event;

import java.util.List;

import ru.practicum.ewm.event.dto.EventFiltersForAdmin;
import ru.practicum.ewm.event.dto.EventFiltersForPublic;
import ru.practicum.ewm.event.dto.EventForResponseDto;
import ru.practicum.ewm.event.dto.EventShortedForResponseDto;
import ru.practicum.ewm.event.dto.EventToAddDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.dto.UpdateRequestsStatusDto;
import ru.practicum.ewm.event.dto.UpdatedRequestsStatusDto;
import ru.practicum.ewm.request.RequestDto;

public interface EventService {

    List<EventShortedForResponseDto> getInitiatorEvents(int userId, int from, int size);

    EventForResponseDto addEvent(int userId, EventToAddDto eventToAddDto);

    EventForResponseDto getEventByInitiator(int userId, int eventId);

    EventForResponseDto updateEventByInitiator(int userId, int eventId, UpdateEventDto updateEventDto);

    List<RequestDto> getRequestsForInitiatorEvent(int userId, int eventId);

    UpdatedRequestsStatusDto updateRequestsStatusByInitiator(int userId, int eventId,
                                                             UpdateRequestsStatusDto updateRequestsStatusDto);

    List<EventForResponseDto> getEventsForAdmin(EventFiltersForAdmin eventFiltersForAdmin, int from, int size);

    EventForResponseDto updateEventByAdmin(int eventId, UpdateEventDto updateEventDto);

    List<EventShortedForResponseDto> getEventsForPublic(EventFiltersForPublic eventFiltersForPublic,
                                                        SortEventsBy sort, int from, int size,
                                                        String uri, String ip);

    EventForResponseDto getEventForPublic(int eventId, String uri, String ip);
}
