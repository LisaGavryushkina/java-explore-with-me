package ru.practicum.ewm.request;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{

    @Override
    public List<RequestDto> getRequests(int userId) {
        return null;
    }

    @Override
    public RequestDto addRequest(int userId, int eventId) {
        return null;
    }

    @Override
    public RequestDto cancelRequest(int userId, int requestId) {
        return null;
    }
}
