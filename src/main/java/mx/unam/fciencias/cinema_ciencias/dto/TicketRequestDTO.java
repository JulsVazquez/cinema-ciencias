package mx.unam.fciencias.cinema_ciencias.dto;

import jakarta.validation.constraints.NotNull;

public class TicketRequestDTO {
    @NotNull
    private Long movieScreeningId;

    @NotNull
    private Long userId;

    public Long getMovieScreeningId() {
        return movieScreeningId;
    }

    public void setMovieScreeningId(Long movieScreeningId) {
        this.movieScreeningId = movieScreeningId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TicketRequestDTO{" +
                "movieScreeningId=" + movieScreeningId +
                ", userId=" + userId +
                '}';
    }
}