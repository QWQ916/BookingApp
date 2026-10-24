package ru.my.projects.booking.hall;

public record HallDTO(
        Long id,
        String name,
        String description,
        String arenaName,
        Integer maxCountSeats
) {
    public static HallDTO transfer(Hall hall){
        return new HallDTO(hall.getId(), hall.getName(), hall.getDescription(), hall.getArena().getName() + " (ID = " + hall.getArena().getId() + ")", hall.getMaxCountSeats());
    }
}
