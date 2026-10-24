package ru.my.projects.booking.hall;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/0/halls")
public class HallController {

    private final HallService service;

    public HallController(HallService service){
        this.service = service;
    }

    @GetMapping
    public List<HallDTO> getHalls(){
        return service.getAll().stream().map(HallDTO::transfer).toList();
    }

    @GetMapping("/{id}")
    public HallDTO getHall(Long id){
        return HallDTO.transfer(service.getOne(id));
    }

    @PostMapping
    public ResponseEntity<HallDTO> createHall(@Valid @RequestBody HallCreateDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(HallDTO.transfer(service.create(request.name(), request.description(), request.arenaId(), request.maxCountSeats())));
    }

    @PutMapping("/{id}")
    public HallDTO putHall(@Valid @RequestBody HallCreateDTO request, @PathVariable Long id){
        return HallDTO.transfer(service.put(id, request.name(), request.description(), request.arenaId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
