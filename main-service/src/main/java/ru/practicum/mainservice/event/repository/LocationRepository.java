package ru.practicum.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.event.model.LocationModel;

public interface LocationRepository extends JpaRepository<LocationModel, Long> {
}
