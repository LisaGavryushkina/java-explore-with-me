package ru.practicum.ewm.event;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {

    Page<Event> findAllByInitiatorId(int userId, Pageable pageable);

    List<Event> findAllByCategoryId(int catId);
}
