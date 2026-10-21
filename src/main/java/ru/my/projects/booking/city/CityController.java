package ru.my.projects.booking.city;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/0/cities")
public class CityController {

    private final CityService service;

    public CityController(CityService service){
        this.service = service;
    }

    @Transactional
    @GetMapping
    public List<CityDTO> getCities(){
        return service.getAll().stream().map(CityDTO::transfer).toList();
    }

    @Transactional
    @GetMapping("/{id}")
    public CityDTO getCity(@PathVariable long id){
        return CityDTO.transfer(service.getOne(id));
    }

    @PostMapping
    public ResponseEntity<CityDTO> createCity(@RequestBody CityCreateDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(CityDTO.transfer(service.create(request.name())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> putCity(@PathVariable long id, @RequestBody CityCreateDTO request){
        return ResponseEntity.status(HttpStatus.OK).body(CityDTO.transfer(service.edit(id, request.name())));
    }

    // Решить костыль с LAZY загрузкой (транзакция в контроллере) - решение маппить в DTO с транзакцией или JOIN FETCH
}
