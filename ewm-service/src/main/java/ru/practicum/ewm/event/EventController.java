package ru.practicum.ewm.event;

import java.util.List;

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
                                        @RequestBody @Valid EventForRequestDto eventForRequestDto) {
        return eventService.addEvent(userId, eventForRequestDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventForResponseDto getEvent(@PathVariable int userId, @PathVariable int eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventForResponseDto updateEventByInitiator(@PathVariable int userId, @PathVariable int eventId,
                                                      @RequestBody EventForRequestDto eventForRequestDto) {
        return eventService.updateEventByInitiator(userId, eventId, eventForRequestDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestsForInitiatorEvent(@PathVariable int userId, @PathVariable int eventId) {
        return eventService.getRequestsForInitiatorEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public UpdatedRequestsStatusDto updateRequestsStatusByInitiator(@PathVariable int userId, @PathVariable int eventId,
                                                                    @RequestBody @Valid ToUpdateRequestsStatusDto toUpdateRequestsStatusDto) {
        return eventService.updateRequestsStatusByInitiator(userId, eventId, toUpdateRequestsStatusDto);
    }
}
