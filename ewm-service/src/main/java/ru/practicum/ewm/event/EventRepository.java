package ru.practicum.ewm.event;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.ewm.repository.EwmEntityRepository;

public interface EventRepository extends EwmEntityRepository<Event>, JpaSpecificationExecutor<Event> {

    Page<Event> findAllByInitiatorId(int userId, Pageable pageable);

    List<Event> findAllByCategoryId(int catId);
}
