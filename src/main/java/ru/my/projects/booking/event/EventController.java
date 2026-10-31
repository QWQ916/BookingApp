package ru.my.projects.booking.event;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/0")
public class EventController {

    private final EventService service;
    public EventController(EventService service){
        this.service = service;
    }

    @GetMapping("/arenas/{arena_id}/halls/{hall_id}/events")
    public List<EventDTO> getEventsNested(@RequestParam(name = "arena_id") Long arenaId, @RequestParam(name = "hall_id") Long hallId){
        return service.getAllNested(arenaId, hallId).stream().map(EventDTO::transfer).toList();
    }

    @GetMapping("/events")
    public List<EventDTO> getEventsFlat(){
        return service.getAllFlat().stream().map(EventDTO::transfer).toList();
    }

    @GetMapping("/events/{id}")
    public EventDTO getEvent(Long id){
        return EventDTO.transfer(service.getOne(id));
    }

    @PostMapping("/arenas/{arena_id}/halls/{hall_id}/events")
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestParam(name = "arena_id") Long arenaId, @RequestParam(name = "hall_id") Long hallId, @Valid @RequestBody EventCreateDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(EventDTO.transfer(service.create(arenaId, hallId, request.title(), request.startsAt(), request.endsAt(), request.price(), request.ageRestrict())));
    }

    @PutMapping("/events/{id}")
    public EventDTO putEvent(@RequestParam Long id, @Valid @RequestBody EventCreateDTO request){
        return EventDTO.transfer(service.put(id, request.title(), request.startsAt(), request.endsAt(), request.price(), request.ageRestrict()));
    }

    @PatchMapping("/events/{id}")
    public EventDTO patchEvent(@RequestParam Long id, @Valid @RequestBody EventPatchDTO request){
        return EventDTO.transfer(service.patch(id, request.title(), request.startsAt(), request.endsAt(), request.hallId()));
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
