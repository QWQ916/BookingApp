package ru.my.projects.booking.seat;

import jakarta.persistence.*;
import lombok.Getter;
import ru.my.projects.booking.hall.Hall;

@Entity
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(name = "uk_hall_row_seat", columnNames = {"hall_id", "row", "seat_num"})
})
public class Seat {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private Integer row;

    @Getter
    private Integer seatNum;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    public Seat(Integer row, Integer seatNum, Hall hall){
        this.row = row; this.seatNum = seatNum; this.hall = hall;
    }
}
