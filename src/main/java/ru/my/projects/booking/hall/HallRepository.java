package ru.my.projects.booking.hall;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HallRepository extends JpaRepository<Hall, Long> {

    @EntityGraph(attributePaths = "arena")
    @Override
    List<Hall> findAll();

    @Override
    @EntityGraph(attributePaths = "arena")
    Optional<Hall> findById(Long id);
}
