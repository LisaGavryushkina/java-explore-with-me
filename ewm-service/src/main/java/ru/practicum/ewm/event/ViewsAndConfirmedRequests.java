package ru.practicum.ewm.event;

import lombok.Data;

@Data
public class ViewsAndConfirmedRequests {
    private final int views;
    private final int confirmedRequests;
}
