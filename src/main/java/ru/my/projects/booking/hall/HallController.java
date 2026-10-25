package ru.my.projects.booking.hall;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/0/arenas")
public class HallController {

    private final HallService service;

    public HallController(HallService service){
        this.service = service;
    }

    @GetMapping("/halls")
    public List<HallDTO> getHalls(@RequestParam(required = false) String name){
        return service.getAll(name).stream().map(HallDTO::transfer).toList();
    }

    @GetMapping("/halls/{id}")
    public HallDTO getHall(@PathVariable Long id){
        return HallDTO.transfer(service.getOne(id));
    }

    @GetMapping("/{arena_id}/halls")
    public List<HallDTO> getHallsForArena(@PathVariable(name = "arena_id") Long arenaId, @RequestParam(required = false) String name){
        return service.getHallsForArena(arenaId, name).stream().map(HallDTO::transfer).toList();
    }

    @PostMapping("/{arena_id}/hall")
    public ResponseEntity<HallDTO> createHall(@Valid @RequestBody HallCreateDTO request, @PathVariable(name = "arena_id") Long arenaId){
        return ResponseEntity.status(HttpStatus.CREATED).body(HallDTO.transfer(service.create(request.name(), request.description(), arenaId, request.maxCountSeats())));
    }

    @PutMapping("/{arena_id}/hall/{id}")
    public HallDTO putHall(@Valid @RequestBody HallCreateDTO request, @PathVariable(name = "id") Long id, @PathVariable(name = "arena_id") Long arenaId){
        return HallDTO.transfer(service.put(id, request.name(), request.description(), arenaId));
    }

    @DeleteMapping("/halls/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
