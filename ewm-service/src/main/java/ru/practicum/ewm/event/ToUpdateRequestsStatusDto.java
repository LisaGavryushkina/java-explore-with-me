package ru.practicum.ewm.event;

import java.util.List;

import lombok.Data;
import ru.practicum.ewm.request.Status;

@Data
public class ToUpdateRequestsStatusDto {

    private final List<Integer> requestsIds;
    private final Status status;
}
