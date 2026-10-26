package mx.unam.fciencias.cinema_ciencias.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class MovieScreeningRequestDTO {
    @Valid
    @NotNull
    private TheaterRequestDTO theater;
    private String movie;
    private LocalDateTime schedule;

    public MovieScreeningRequestDTO() {}

    public MovieScreeningRequestDTO(TheaterRequestDTO theater, String movie, LocalDateTime schedule) {
        this.theater = theater;
        this.movie = movie;
        this.schedule = schedule;
    }

    public TheaterRequestDTO getTheater() {
        return theater;
    }

    public void setTheater(TheaterRequestDTO theater) {
        this.theater = theater;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public LocalDateTime getSchedule() {
        return schedule;
    }

    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }
}