package mx.unam.fciencias.cinema_ciencias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TheaterRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private LocationRequestDTO location;

    @NotNull
    private Integer seats;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocationRequestDTO getLocation() { return location; }
    public void setLocation(LocationRequestDTO location) { this.location = location; }
    public Integer getSeats() { return seats; }
    public void setSeats(Integer seats) { this.seats = seats; }
}