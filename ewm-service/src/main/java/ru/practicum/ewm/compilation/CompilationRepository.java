package ru.practicum.ewm.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.repository.EwmEntityRepository;

public interface CompilationRepository extends EwmEntityRepository<Compilation> {

    Page<Compilation> findAllByPinned(boolean pinned, Pageable pageable);
}
