package ru.practicum.ewm.compilation;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.Event;

public interface CompilationAndEventRepository extends JpaRepository<CompilationAndEvent, Integer> {

    void deleteAllByCompilationId(int compilationId);

    @Query(" select event " +
            " from CompilationAndEvent as ce " +
            " join ce.event as event " +
            " join ce.compilation as comp " +
            " where comp.id = :compilationId ")
    List<Event> findAllEventByCompilationId(int compilationId);

    List<CompilationAndEvent> findAllByCompilationIn(Collection<Compilation> compilations);

}
