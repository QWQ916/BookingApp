package ru.my.projects.booking.arena;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/0/arenas")
public class ArenaController {

    private final ArenaService service;

    public ArenaController(ArenaService service){
        this.service = service;
    }

    @GetMapping
    public List<ArenaDTO> getAll(){
        return service.getAll().stream().map(ArenaDTO::transfer).toList();
    }

    @GetMapping("/{id}")
    public ArenaDTO getOne(@PathVariable long id){
        return ArenaDTO.transfer(service.getOne(id));
    }

}
