package mx.unam.fciencias.cinema_ciencias.dto;

import java.time.LocalDateTime;

public class ReservationResponseDTO {

    private Long id;
    private Long userId;
    private Long movieScreeningId;
    private String movieTitle;
    private Integer seatNumber;
    private LocalDateTime reservationTime;

    public ReservationResponseDTO() {}

    public ReservationResponseDTO(Long id, Long userId, Long movieScreeningId, String movieTitle, LocalDateTime reservationTime) {
        this.id = id;
        this.userId = userId;
        this.movieScreeningId = movieScreeningId;
        this.movieTitle = movieTitle;
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

    public Long getMovieScreeningId() {
        return movieScreeningId;
    }

    public void setMovieScreeningId(Long movieScreeningId) {
        this.movieScreeningId = movieScreeningId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }
}
