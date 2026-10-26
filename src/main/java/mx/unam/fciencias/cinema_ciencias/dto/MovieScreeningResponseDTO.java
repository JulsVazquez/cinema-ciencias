package mx.unam.fciencias.cinema_ciencias.dto;

import java.time.LocalDateTime;

public class MovieScreeningResponseDTO {

    private Long id;
    private TheaterResponseDTO theater;
    private String movie;
    private LocalDateTime schedule;

    public MovieScreeningResponseDTO() {}

    public MovieScreeningResponseDTO(Long id, TheaterResponseDTO theater, String movie, LocalDateTime schedule) {
        this.id = id;
        this.theater = theater;
        this.movie = movie;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TheaterResponseDTO getTheater() {
        return theater;
    }

    public void setTheater(TheaterResponseDTO theater) {
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