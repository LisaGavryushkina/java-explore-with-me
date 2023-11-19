package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.practicum.ewm.error_handler.EwmEntityNotFoundException;

@NoRepositoryBean
public interface EwmEntityRepository<T> extends JpaRepository<T, Integer> {

    default T findByIdOrThrow(int id) {
        return findById(id).orElseThrow(() -> new EwmEntityNotFoundException(id));
    }

    default void checkEntityExists(int id) {
        findByIdOrThrow(id);
    }
}
