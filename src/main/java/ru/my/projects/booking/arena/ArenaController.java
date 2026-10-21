package ru.my.projects.booking.arena;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/0/arenas")
public class ArenaController {

    private final ArenaService service;

    public ArenaController(ArenaService service){
        this.service = service;
    }

    @Transactional
    @GetMapping
    public List<ArenaDTO> getAll(){
        return service.getAll().stream().map(ArenaDTO::transfer).toList();
    }

    @Transactional
    @GetMapping("/{id}")
    public ArenaDTO getOne(@PathVariable long id){
        return ArenaDTO.transfer(service.getOne(id));
    }

    @PostMapping
    public ResponseEntity<ArenaDTO> createArena(@Valid @RequestBody ArenaCreateDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ArenaDTO.transfer(service.create(request.name(), request.description(), request.address(), request.cityName())));
    }

    @PutMapping("/{id}")
    public ArenaDTO putArena(@Valid @RequestBody ArenaCreateDTO request, long id){
        return ArenaDTO.transfer(service.put(id, request.name(), request.description(), request.address(), request.cityName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArena(@PathVariable long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Решить костыль с LAZY загрузкой (транзакция в контроллере) - решение маппить в DTO с транзакцией или JOIN FETCH
}
