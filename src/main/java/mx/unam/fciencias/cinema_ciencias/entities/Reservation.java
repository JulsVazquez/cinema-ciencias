package mx.unam.fciencias.cinema_ciencias.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "movie_screening_id", nullable = false)
    private MovieScreening movieScreening;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "reservation_time")
    private LocalDateTime reservationTime;

    public Reservation() {}

    public Reservation(Long userId, MovieScreening movieScreening, Integer seatNumber, LocalDateTime reservationTime) {
        this.userId = userId;
        this.movieScreening = movieScreening;
        this.seatNumber = seatNumber;
        this.reservationTime = reservationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MovieScreening getMovieScreening() {
        return movieScreening;
    }

    public void setMovieScreening(MovieScreening movieScreening) {
        this.movieScreening = movieScreening;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }
}