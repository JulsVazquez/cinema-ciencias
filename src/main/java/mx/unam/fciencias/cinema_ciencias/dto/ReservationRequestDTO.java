package mx.unam.fciencias.cinema_ciencias.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReservationRequestDTO {

    @NotNull
    private Long userId;

    @NotNull
    private Long movieScreeningId;

    @Min(1)
    @Max(100)
    private Integer seatNumber;

    public ReservationRequestDTO() {}

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

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }
}