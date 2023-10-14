package ru.practicum.ewm.user;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.rating.LikesAndTotal;
import ru.practicum.ewm.repository.EwmEntityRepository;

public interface UserRepository extends EwmEntityRepository<User> {

    Page<User> findAllByIdIn(List<Integer> ids, Pageable pageable);

    @Query(nativeQuery = true, value = " " +
            " select e.initiator_id as entityId, " +
            " count(case when l.is_like then 1 end) as likes, " +
            " count(*) as total " +
            " from events as e " +
            " join likes as l on e.id = l.event_id " +
            " where e.initiator_id in :userIds " +
            " group by e.initiator_id ")
    List<LikesAndTotal> countLikesAndTotalForUsers(Collection<Integer> userIds);
}
