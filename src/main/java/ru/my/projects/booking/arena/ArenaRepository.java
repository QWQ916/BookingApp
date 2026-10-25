package ru.my.projects.booking.arena;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArenaRepository extends JpaRepository<Arena, Long> {

    @Override
    @EntityGraph(attributePaths = "city")
    List<Arena> findAll();

    @Override
    @EntityGraph(attributePaths = "city")
    Optional<Arena> findById(Long id);

    @EntityGraph(attributePaths = "city")
    List<Arena> findByNameContainsIgnoreCase(String name);
}
