package ru.practicum.ewm.event;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;
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
    @Size(max = 1000)
    @Setter
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @Setter
    private Category category;

    @Column(name = "created", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "description", nullable = false)
    @Size(max = 1000)
    @Setter
    private String description;

    @Column(name = "event_date", nullable = false)
    @Setter
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "latitude", nullable = false)
    @Setter
    private long lat;

    @Column(name = "longitude", nullable = false)
    @Setter
    private long lon;

    @Column(name = "is_paid", nullable = false)
    @Setter
    private boolean paid;

    @Column(name = "participant_limit", nullable = false)
    @Setter
    private int participantLimit;

    @Formula("(participant_limit = 0 " +
            "or (select count(*) where requests.event_id = events.id " +
            "and requests.status = 'CONFIRMED'))")
    private boolean isAvailable;

    @Column(name = "published")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation", nullable = false)
    @Setter
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "state", nullable = false)
    @Setter
    private State state;

    @Column(name = "title", nullable = false)
    @Setter
    private String title;

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
