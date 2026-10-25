package ru.my.projects.booking.city;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findCityByName(String cityName);

    List<City> findByNameContainsIgnoreCase(String name);
}
