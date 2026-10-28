package ru.my.projects.booking.arena;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/0")
public class ArenaController {

    private final ArenaService service;

    public ArenaController(ArenaService service){
        this.service = service;
    }

    @GetMapping("/arenas")
    public List<ArenaDTO> getArenasFlat(@RequestParam(required = false) String name){
        return service.getAllFlat(name).stream().map(ArenaDTO::transfer).toList();
    }

    @GetMapping("/arenas/{id}")
    public ArenaDTO getArena(@PathVariable long id){
        return ArenaDTO.transfer(service.getOne(id));
    }

    @GetMapping("/cities/{city_id}/arenas")
    public List<ArenaDTO> getArenasNested(@PathVariable(name = "city_id") Long cityId){
        return service.getAllNested(cityId).stream().map(ArenaDTO::transfer).toList();
    }

    @PostMapping("/arenas")
    public ResponseEntity<ArenaDTO> createArena(@Valid @RequestBody ArenaCreateDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ArenaDTO.transfer(service.create(request.name(), request.description(), request.address(), request.cityName())));
    }

    @PutMapping("/arenas/{id}")
    public ArenaDTO putArena(@Valid @RequestBody ArenaCreateDTO request, @PathVariable long id){
        return ArenaDTO.transfer(service.put(id, request.name(), request.description(), request.address(), request.cityName()));
    }

    @DeleteMapping("/arenas/{id}")
    public ResponseEntity<Void> deleteArena(@PathVariable long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
