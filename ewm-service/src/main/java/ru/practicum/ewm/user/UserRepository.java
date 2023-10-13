package ru.practicum.ewm.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findAllByIdIn(List<Integer> ids, Pageable pageable);

    @Query(nativeQuery = true, value = " " +
            " select count(case when l.is_like then 1 end) as likes, " +
            " count(*) as total " +
            " from events as e " +
            " join likes as l on e.id = l.event_id " +
            " where e.initiator_id = :userId ")
    LikesAndTotal countLikesAndTotalForInitiator(int userId);

    interface LikesAndTotal {
        int getLikes();
        int getTotal();
    }
}
