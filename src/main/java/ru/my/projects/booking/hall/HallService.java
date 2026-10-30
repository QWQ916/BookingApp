package ru.my.projects.booking.hall;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.my.projects.booking.arena.Arena;
import ru.my.projects.booking.arena.ArenaRepository;
import ru.my.projects.booking.arena.ArenaService;
import ru.my.projects.booking.common.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class HallService {

    private final HallRepository hallRepo;
    private final ArenaService arenaServ;

    public HallService(HallRepository hallRepo, ArenaRepository arenaRepo, ArenaService arenaServ){
        this.hallRepo = hallRepo; this.arenaServ = arenaServ;
    }

    @Transactional(readOnly = true)
    public List<Hall> getAllFlat(String name){
        if (name != null && !name.isBlank()){
            return hallRepo.findByNameContainsIgnoreCase(name);
        }

        return hallRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Hall getOne(long id){
        return hallRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Зал с ID = " + id + " не найден!"));
    }


    @Transactional(readOnly = true)
    public List<Hall> getAllNested(Long arenaId, String name){
        Arena arena = arenaServ.getOne(arenaId);

        if (name != null && !name.isBlank()){
            return hallRepo.findByArenaIdAndNameContainsIgnoreCase(arenaId, name);
        }
        return hallRepo.findByArenaId(arenaId);
    }

    @Transactional
    public Hall create(String name, String description, Long arenaId, Integer rowsCount, Integer seatsInRows){
        Arena arena = arenaServ.getOne(arenaId);
        Hall newHall = new Hall(name, description, arena, seatsInRows, rowsCount);
        hallRepo.save(newHall);
        return newHall;
    }

    @Transactional
    public Hall patch(Long id, Integer rowsCount, Integer seatsInRows){
        Hall hall = hallRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Невозможно редактировать зал, так как зала с ID = " + id + " не существует!"));
        if (rowsCount != null) hall.setRowsCount(rowsCount);
        if (seatsInRows != null) hall.setSeatsInRows(seatsInRows);
        hallRepo.save(hall);

        return hall;
    }

    @Transactional
    public Hall put(Long id, String name, String description, Integer rowsCount, Integer seatsInRows){
        Hall hall = hallRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Невозможно редактировать зал, так как зала с ID = " + id + " не существует!"));
        hall.setName(name);
        hall.setDescription(description);
        hall.setChangedAt(LocalDateTime.now());

        hall.setRowsCount(rowsCount);
        hall.setSeatsInRows(seatsInRows);

        hallRepo.save(hall);
        return hall;
    }

    @Transactional
    public void delete(Long id){
        Hall hall = hallRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Невозможно удалить зал, так как зала с ID = " + id + " не существует!"));
        hallRepo.delete(hall);
    }
}
