package ru.practicum.ewm.event;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventFiltersForAdmin;
import ru.practicum.ewm.event.dto.EventFiltersForPublic;
import ru.practicum.ewm.event.dto.EventForResponseDto;
import ru.practicum.ewm.event.dto.EventShortedForResponseDto;
import ru.practicum.ewm.event.dto.EventToAddDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.dto.UpdateRequestsStatusDto;
import ru.practicum.ewm.event.dto.UpdatedRequestsStatusDto;
import ru.practicum.ewm.request.RequestDto;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/users/{userId}/events")
    public List<EventShortedForResponseDto> getInitiatorEvents(@PathVariable int userId,
                                                               @RequestParam(defaultValue = "0") int from,
                                                               @RequestParam(defaultValue = "10") int size) {
        return eventService.getInitiatorEvents(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventForResponseDto addEvent(@PathVariable int userId,
                                        @RequestBody @Valid EventToAddDto eventToAddDto) {
        return eventService.addEvent(userId, eventToAddDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventForResponseDto getEventByInitiator(@PathVariable int userId, @PathVariable int eventId) {
        return eventService.getEventByInitiator(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventForResponseDto updateEventByInitiator(@PathVariable int userId, @PathVariable int eventId,
                                                      @RequestBody @Valid UpdateEventDto updateEventDto) {
        return eventService.updateEventByInitiator(userId, eventId, updateEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestsForInitiatorEvent(@PathVariable int userId, @PathVariable int eventId) {
        return eventService.getRequestsForInitiatorEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public UpdatedRequestsStatusDto updateRequestsStatusByInitiator(@PathVariable int userId, @PathVariable int eventId,
                                                                    @RequestBody UpdateRequestsStatusDto updateRequestsStatusDto) {
        return eventService.updateRequestsStatusByInitiator(userId, eventId, updateRequestsStatusDto);
    }

    @GetMapping("/admin/events")
    public List<EventForResponseDto> getEventsForAdmin(@Valid EventFiltersForAdmin eventFiltersForAdmin,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsForAdmin(eventFiltersForAdmin, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventForResponseDto updateEventByAdmin(@PathVariable int eventId,
                                                  @RequestBody @Valid UpdateEventDto updateEventDto) {
        return eventService.updateEventByAdmin(eventId, updateEventDto);
    }

    @GetMapping("/events")
    public List<EventShortedForResponseDto> getEventsForPublic(@Valid EventFiltersForPublic eventFiltersForPublic,
                                                               @RequestParam(defaultValue = "VIEWS") SortEventsBy sort,
                                                               @RequestParam(defaultValue = "0") int from,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        return eventService.getEventsForPublic(eventFiltersForPublic, sort, from, size, uri, ip);
    }

    @GetMapping("/events/{id}")
    public EventForResponseDto getEventForPublic(@PathVariable int id, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        return eventService.getEventForPublic(id, uri, ip);
    }
}
