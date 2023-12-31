package ru.practicum.mainservice.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.user.model.UserModel;

import java.util.List;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    List<UserModel> findAllByIdIn(List<Long> ids, Pageable pageable);
}
