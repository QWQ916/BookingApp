package ru.my.projects.booking.hall;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.my.projects.booking.arena.Arena;
import ru.my.projects.booking.arena.ArenaRepository;
import ru.my.projects.booking.common.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HallService {

    private final HallRepository hallRepo;
    private final ArenaRepository arenaRepo;

    public HallService(HallRepository hallRepo, ArenaRepository arenaRepo){
        this.hallRepo = hallRepo; this.arenaRepo = arenaRepo;
    }

    @Transactional(readOnly = true)
    public List<Hall> getAll(String name){
        if (name != null && !name.isBlank()){
            return hallRepo.findByNameContainsIgnoreCase(name);
        }

        return hallRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Hall getOne(long id){
        return hallRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Залл с ID = " + id + " не найден!"));
    }

    @Transactional(readOnly = true)
    public List<Hall> getHallsForArena(Long arenaId, String name){
        Arena arena = arenaRepo.findById(arenaId).orElseThrow(() -> new ResourceNotFoundException("Арены с заданным ID = " + arenaId + " не существует!"));

        if (name != null && !name.isBlank()){
            return hallRepo.findByArenaIdAndNameContainsIgnoreCase(arenaId, name);
        }
        return hallRepo.findByArenaId(arenaId);
    }

    @Transactional
    public Hall create(String name, String description, Long arenaId, Integer maxCountSeats){
        Arena arena = arenaRepo.findById(arenaId).orElseThrow(() -> new ResourceNotFoundException("Невозможно создать зал, так как арены с ID = " + arenaId + " не существует!"));
        Hall newHall = new Hall(name, description, arena, maxCountSeats);
        hallRepo.save(newHall);
        return newHall;
    }

    @Transactional
    public Hall put(Long id, String name, String description, Long newArenaId){
        Arena arena = arenaRepo.findById(newArenaId).orElseThrow(() -> new ResourceNotFoundException("Невозможно редактировать зал, так как арены с ID = " + newArenaId + " не существует!"));
        Hall hall = hallRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Невозможно редактировать зал, так как зала с ID = " + id + " не существует!"));
        hall.setName(name);
        hall.setDescription(description);
        hall.setChangedAt(LocalDateTime.now());
        hall.setArena(arena);
        hallRepo.save(hall);
        return hall;
    }

    @Transactional
    public void delete(Long id){
        Hall hall = hallRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Невозможно удалить зал, так как зала с ID = " + id + " не существует!"));
        hallRepo.delete(hall);
    }
}
