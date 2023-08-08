package ru.practicum.mainservice.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.compilation.model.CompilationModel;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<CompilationModel, Long> {

    List<CompilationModel> findAllByPinned(Boolean pinned, Pageable pageable);
}
