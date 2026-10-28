package ru.my.projects.booking.seat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.my.projects.booking.arena.Arena;
import ru.my.projects.booking.arena.ArenaService;
import ru.my.projects.booking.common.exception.ResourceNotFoundException;
import ru.my.projects.booking.hall.Hall;
import ru.my.projects.booking.hall.HallRepository;
import ru.my.projects.booking.hall.HallService;

import java.util.List;
import java.util.Objects;

@Service
public class SeatService {

    private final SeatRepository seatRepo;
    private final ArenaService arenaServ;
    private final HallService hallServ;

    public SeatService(SeatRepository seatRepo, HallRepository hallRepo, ArenaService arenaServ, HallService hallServ){
        this.seatRepo = seatRepo; this.arenaServ = arenaServ; this.hallServ = hallServ;
    }

    @Transactional(readOnly = true)
    public List<Seat> getAllNested(Long arenaId, Long hallId){
        Arena arena = arenaServ.getOne(arenaId);
        Hall hall = hallServ.getOne(hallId);

        if (!Objects.equals(arenaId, hall.getArena().getId())){
            throw new ResourceNotFoundException("У данной арены с ID = " + arenaId + " нет заданного зала с ID = " + hallId);
        }

        return seatRepo.findByHallId(hallId);
    }

    @Transactional(readOnly = true)
    public List<Seat> getAllFlat(){
        return seatRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Seat getOne(Long id){
        return seatRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Места с указанным ID = " + id + " не существует!"));
    }

    @Transactional
    public Seat create(Long arenaId, Long hallId, Integer row, Integer seatNum){
        Arena arena = arenaServ.getOne(arenaId);
        Hall hall = hallServ.getOne(hallId);

        if (!Objects.equals(arenaId, hall.getArena().getId())){
            throw new ResourceNotFoundException("У данной арены с ID = " + arenaId + " нет заданного зала с ID = " + hallId);
        }

        Seat seat = new Seat(row, seatNum, hall);
        seatRepo.save(seat);
        return seat;
    }

    @Transactional
    public Seat put(Long id, Integer row, Integer seatNum){
        Seat newSeat = seatRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Невозможно отредактировать место с ID = " + id + ", так как его не существует!"));
        newSeat.setRow(row);
        newSeat.setSeatNum(seatNum);
        seatRepo.save(newSeat);
        return newSeat;
    }

    @Transactional
    public void delete(Long id){
        Seat seat = seatRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Нечего удалять, места с ID = " + id + " не существует!"));
        seatRepo.delete(seat);
    }
}
