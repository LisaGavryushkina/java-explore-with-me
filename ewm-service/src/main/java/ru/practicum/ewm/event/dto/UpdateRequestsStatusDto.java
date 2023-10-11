package ru.practicum.ewm.event.dto;

import java.util.List;

import lombok.Data;
import ru.practicum.ewm.request.Status;

@Data
public class UpdateRequestsStatusDto {

    private final List<Integer> requestsIds;
    private final Status status;
}
