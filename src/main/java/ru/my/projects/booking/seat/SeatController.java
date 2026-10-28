package ru.my.projects.booking.seat;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/0")
public class SeatController {

    private final SeatService service;

    public SeatController(SeatService service){
        this.service = service;
    }

    @GetMapping("/arenas/{arena_id}/halls/{hall_id}/seats")
    public List<SeatDTO> getSeatsNested(@PathVariable(name = "arena_id") Long arenaId, @PathVariable(name = "hall_id") Long hallId){
        return service.getAllNested(arenaId, hallId).stream().map(SeatDTO::transfer).toList();
    }

    @GetMapping("/seats")
    public List<SeatDTO> getSeatsFlat(){
        return service.getAllFlat().stream().map(SeatDTO::transfer).toList();
    }

    @GetMapping("/seats/{seat_id}")
    public SeatDTO getSeat(@PathVariable(name = "seat_id") Long id){
        return SeatDTO.transfer(service.getOne(id));
    }

    @PostMapping("/arenas/{arena_id}/halls/{hall_id}/seats")
    public ResponseEntity<SeatDTO> createSeat(@PathVariable(name = "arena_id") Long arenaId, @PathVariable(name = "hall_id") Long hallId, @Valid @RequestBody SeatCreateDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(SeatDTO.transfer(service.create(arenaId, hallId, request.row(), request.seat_num())));
    }

    @PutMapping("/seats/{id}")
    public SeatDTO putSeat(@PathVariable Long id, @Valid @RequestBody SeatCreateDTO request){
        return SeatDTO.transfer(service.put(id, request.row(), request.seat_num()));
    }

    @DeleteMapping("/seats/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
