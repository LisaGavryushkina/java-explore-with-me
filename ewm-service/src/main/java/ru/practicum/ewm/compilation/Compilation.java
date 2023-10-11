package ru.practicum.ewm.compilation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "compilations")
@NoArgsConstructor
@ToString
@Getter
@Setter
@AllArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;

    @Column(name = "title", nullable = false)
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compilation)) {
            return false;
        }
        return id == ((Compilation) o).getId();
    }

    @Override
    public int hashCode() {
        return id;
    }


}
