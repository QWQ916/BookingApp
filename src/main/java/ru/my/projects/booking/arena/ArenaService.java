package ru.my.projects.booking.arena;

import org.springframework.stereotype.Service;
import ru.my.projects.booking.city.City;
import ru.my.projects.booking.city.CityRepository;
import ru.my.projects.booking.common.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class ArenaService {

    private final ArenaRepository arenaRepo;
    private final CityRepository cityRepo;

    public ArenaService(ArenaRepository arenaRepo, CityRepository cityRepo){
        this.arenaRepo = arenaRepo;
        this.cityRepo = cityRepo;
    }

    public List<Arena> getAll(){
        return arenaRepo.findAll();
    }

    public Arena getOne(long id){
        return arenaRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Арена с ID = " + id + " не найдена!"));
    }

    public Arena create(String name, String description, String address, City city){
        return new Arena(name, description, address, city);
    }
}
