package ru.my.projects.booking.event;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = {"hall", "hall.arena"})
    @Override
    List<Event> findAll();

    @EntityGraph(attributePaths = {"hall", "hall.arena"})
    List<Event> findByHallId(Long hallId);

    @EntityGraph(attributePaths = {"hall", "hall.arena"})
    @Override
    Optional<Event> findById(Long id);
}
