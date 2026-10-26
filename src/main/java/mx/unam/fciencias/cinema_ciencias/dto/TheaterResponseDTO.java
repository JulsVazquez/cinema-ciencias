package mx.unam.fciencias.cinema_ciencias.dto;

import java.io.Serializable;

public class TheaterResponseDTO implements Serializable {
    private String name;
    private Integer seats;

    public TheaterResponseDTO() {}

    public TheaterResponseDTO(String name, Integer seats) {
        this.name = name;
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}