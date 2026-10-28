package ru.my.projects.booking.seat;

public record SeatDTO(
        Long id,
        Integer row,
        Integer seatNum,
        String hallName
) {
    public static SeatDTO transfer(Seat seat){
        return new SeatDTO(seat.getId(), seat.getRow(), seat.getSeatNum(), seat.getHall().getName());
    }
}
