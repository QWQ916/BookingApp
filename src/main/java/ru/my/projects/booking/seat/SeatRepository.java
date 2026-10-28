package ru.my.projects.booking.seat;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @EntityGraph(attributePaths = { "hall" })
    List<Seat> findByHallId(Long hallId);

    @EntityGraph(attributePaths = "hall")
    @Override
    List<Seat> findAll();

    @EntityGraph(attributePaths = "hall")
    @Override
    Optional<Seat> findById(Long id);
}
