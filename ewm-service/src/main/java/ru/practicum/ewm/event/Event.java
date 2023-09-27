package ru.practicum.ewm.event;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.user.User;

@Entity
@Table(name = "events")
@NoArgsConstructor
@ToString
@Getter
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmedRequests", nullable = false)
    private int confirmedRequests;

    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "eventDate", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "latitude", nullable = false)
    private long lat;

    @Column(name = "longitude", nullable = false)
    private long lon;

    @Column(name = "paid", nullable = false)
    private boolean paid;

    @Column(name = "participantLimit", nullable = false)
    private int participantLimit;

    @Column(name = "publishedOn", nullable = false)
    private LocalDateTime publishedOn;

    @Column(name = "requestModeration", nullable = false)
    private boolean requestModeration;

    @Column(name = "state", nullable = false)
    private State state;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "views", nullable = false)
    private int views;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id == ((Event) o).getId();
    }

    @Override
    public int hashCode() {
        return id;
    }

}
