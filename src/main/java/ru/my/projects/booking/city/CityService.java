package ru.my.projects.booking.city;

import org.springframework.stereotype.Service;
import ru.my.projects.booking.common.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepo;
    public CityService(CityRepository cityRepo){
        this.cityRepo = cityRepo;
    }

    public List<City> getAll(){
        return cityRepo.findAll();
    }

    public City getOne(long id){
        return cityRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Город с указанным ID = " + id + " не найден!"));
    }

    public City create(String name){
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        City newCity = new City(name);
        cityRepo.save(newCity);
        return newCity;
    }

    public void delete(long id){
        City city = cityRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Нечего удалять! Такого города с ID = " + id + " не существует!"));
        cityRepo.delete(city);
    }

    public City edit(long id, String name){
        City city = cityRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Нечего редактировать! Такого города с ID = " + id + " не существует!"));
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        city.setName(name);
        cityRepo.save(city);
        return city;
    }
}
