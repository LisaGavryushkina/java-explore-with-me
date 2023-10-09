package ru.practicum.ewm.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    default Compilation findByIdOrThrow(int compId) {
        return findById(compId).orElseThrow(() -> new CompilationNotFoundException(compId));
    }

    Page<Compilation> findAllByPinned(boolean pinned, Pageable pageable);
}
