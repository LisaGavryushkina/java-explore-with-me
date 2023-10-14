package ru.practicum.ewm.event;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.rating.LikesAndTotal;
import ru.practicum.ewm.repository.EwmEntityRepository;

public interface EventRepository extends EwmEntityRepository<Event>, JpaSpecificationExecutor<Event> {

    Page<Event> findAllByInitiatorId(int userId, Pageable pageable);

    List<Event> findAllByCategoryId(int catId);

    @Query(nativeQuery = true, value = " " +
            " select e.id as entityId, " +
            " count(case when l.is_like then 1 end) as likes, " +
            " count(*) as total " +
            " from events as e " +
            " join likes as l on e.id = l.event_id " +
            " where e.id in :eventIds " +
            " group by e.id ")
    List<LikesAndTotal> countLikesAndTotalForEvents(Collection<Integer> eventIds);
}
