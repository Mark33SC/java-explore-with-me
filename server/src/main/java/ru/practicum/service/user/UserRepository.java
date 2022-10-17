package ru.practicum.service.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("FROM User WHERE id in :ids or :ids is null")
    Collection<User> findAllById(List<Long> ids, PageRequest pageRequest);
}