package ru.practicum.ewm.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RequestDto {

    private final int id;
    private final LocalDateTime created;
    private final int event;
    private final int requester;
    private final Status status;

}
