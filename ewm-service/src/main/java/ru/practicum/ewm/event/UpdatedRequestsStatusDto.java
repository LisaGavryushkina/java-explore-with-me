package ru.practicum.ewm.event;

import java.util.List;

import lombok.Data;
import ru.practicum.ewm.request.RequestDto;

@Data
public class UpdatedRequestsStatusDto {
    private final List<RequestDto> confirmedRequests;
    private final List<RequestDto> rejectedRequests;
}
