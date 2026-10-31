package ru.my.projects.booking.event;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.my.projects.booking.arena.Arena;
import ru.my.projects.booking.arena.ArenaService;
import ru.my.projects.booking.common.exception.ResourceNotFoundException;
import ru.my.projects.booking.hall.Hall;
import ru.my.projects.booking.hall.HallService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class EventService {

    private final EventRepository eventRepo;
    private final ArenaService arenaServ;
    private final HallService hallServ;

    public EventService(EventRepository eventRepo, ArenaService arenaServ, HallService hallServ){
        this.eventRepo = eventRepo; this.arenaServ = arenaServ; this.hallServ = hallServ;
    }

    @Transactional(readOnly = true)
    public List<Event> getAllNested(Long arenaId, Long hallId){
        Arena arena = arenaServ.getOne(arenaId);
        Hall hall = hallServ.getOne(hallId);

        if (!Objects.equals(hall.getArena().getId(), arenaId)) throw new ResourceNotFoundException("У данной арены с ID = " + arenaId + " нет заданного зала с ID = " + hallId);

        return eventRepo.findByHallId(hallId);
    }

    @Transactional(readOnly = true)
    public List<Event> getAllFlat(){
        return eventRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Event getOne(Long id){
        return eventRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Событие с ID = " + id + " не найдено!"));
    }

    @Transactional
    public Event create(Long arenaId, Long hallId, String title, LocalDateTime startsAt, LocalDateTime endsAt, BigDecimal price, Integer ageRestrict){
        Arena arena = arenaServ.getOne(arenaId);
        Hall hall = hallServ.getOne(hallId);

        if (!Objects.equals(hall.getArena().getId(), arenaId)) throw new ResourceNotFoundException("У данной арены с ID = " + arenaId + " нет заданного зала с ID = " + hallId);
        if (endsAt.isBefore(startsAt)) throw new RuntimeException("Не может дата окончания идти до даты начала события!");

        Event event = new Event(title, startsAt, endsAt, price, hall, ageRestrict);
        eventRepo.save(event);

        return event;
    }

    @Transactional
    public Event put(Long id, String title, LocalDateTime startsAt, LocalDateTime endsAt, BigDecimal price, Integer ageRestrict){
        Event event = eventRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Событие с ID = " + id + " не найдено! Нельзя ничего поменять!"));

        if (endsAt.isBefore(startsAt)) throw new RuntimeException("Не может дата окончания идти до даты начала события!");

        event.setAgeRestrict(ageRestrict);
        event.setTitle(title);
        event.setPrice(price);
        event.setStartsAt(startsAt);
        event.setEndsAt(endsAt);

        eventRepo.save(event);
        return event;
    }

    @Transactional
    public Event patch(Long id, String title, LocalDateTime startsAt, LocalDateTime endsAt, Long hallId){
        Event event = eventRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Событие с ID = " + id + " не найдено! Нельзя ничего поменять!"));

        if (hallId != null) { Hall hall = hallServ.getOne(hallId); event.setHall(hall);}
        if (title != null) event.setTitle(title);

        startsAt = startsAt != null ? startsAt : event.getStartsAt();
        endsAt = endsAt != null ? endsAt : event.getEndsAt();

        if (endsAt.isBefore(startsAt)) throw new RuntimeException("Не может дата окончания идти до даты начала события!");

        event.setEndsAt(endsAt);
        event.setStartsAt(startsAt);

        eventRepo.save(event);
        return event;
    }

    @Transactional
    public void delete(Long id){
        Event event = eventRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Событие с ID = " + id + " не найдено! Нельзя ничего удалить!"));

        eventRepo.delete(event);
    }
}
