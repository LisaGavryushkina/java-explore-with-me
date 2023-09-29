package ru.practicum.ewm.request;

import java.util.List;

public interface RequestService {

    List<RequestDto> getRequests(int userId);

    RequestDto addRequest(int userId, int eventId);

    RequestDto cancelRequest(int userId, int requestId);
}
