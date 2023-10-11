package ru.practicum.stats.server;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "hit")
@Data
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;

    @Column(name = "app", nullable = false)
    private final String app;

    @Column(name = "uri", nullable = false)
    private final String uri;

    @Column(name = "ip", nullable = false)
    private final String ip;

    @Column(name = "visited", nullable = false)
    private final LocalDateTime visited;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hit)) {
            return false;
        }
        return id == ((Hit) o).getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
