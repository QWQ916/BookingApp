package ru.my.projects.booking.hall;

public record HallDTO(
        Long id,
        String name,
        String description,
        String arenaName,
        Integer seatsInRows,
        Integer rowsCount
) {
    public static HallDTO transfer(Hall hall){
        return new HallDTO(hall.getId(), hall.getName(), hall.getDescription(), hall.getArena().getName(), hall.getSeatsInRows(), hall.getRowsCount());
    }
}
