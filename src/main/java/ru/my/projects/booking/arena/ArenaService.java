package ru.my.projects.booking.arena;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.my.projects.booking.city.City;
import ru.my.projects.booking.city.CityRepository;
import ru.my.projects.booking.common.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArenaService {

    private final ArenaRepository arenaRepo;
    private final CityRepository cityRepo;

    public ArenaService(ArenaRepository arenaRepo, CityRepository cityRepo){
        this.arenaRepo = arenaRepo;
        this.cityRepo = cityRepo;
    }

    @Transactional
    public List<Arena> getAll(){
        return arenaRepo.findAll();
    }

    @Transactional
    public Arena getOne(long id){
        return arenaRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Арена с ID = " + id + " не найдена!"));
    }

    @Transactional
    public Arena create(String name, String description, String address, String cityName){
        cityName = cityName.substring(0, 1).toUpperCase() + cityName.substring(1).toLowerCase();
        City city = cityRepo.findCityByName(cityName).orElseThrow(() -> new ResourceNotFoundException("Невозможно создать запись, введённого города нет в базе!"));
        Arena arena = new Arena(name, description, address, city);
        arenaRepo.save(arena);
        return arena;
    }

    @Transactional
    public Arena put(long id, String name, String description, String address, String cityName){
        Arena arena = arenaRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Арена не найдена с ID = " + id + ", поэтому её нельзя отредактировать!"));
        cityName = cityName.substring(0, 1).toUpperCase() + cityName.substring(1).toLowerCase();
        City city = cityRepo.findCityByName(cityName).orElseThrow(() -> new ResourceNotFoundException("Невозможно редактировать запись, введённого города нет в базе!"));
        arena.setChangedAt(LocalDateTime.now());
        arena.setName(name);
        arena.setAddress(address);
        arena.setDescription(description);
        arena.setCity(city);
        arenaRepo.save(arena);
        return arena;

    }

    @Transactional
    public void delete(long id){
        Arena arena = arenaRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Арена не найдена с ID = " + id + ", поэтому её нельзя удалить!"));
        arenaRepo.delete(arena);
    }
}
