package mx.unam.fciencias.cinema_ciencias.components;

import org.springframework.stereotype.Component;
import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import mx.unam.fciencias.cinema_ciencias.entities.Location;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterResponseDTO;

@Component
public class TheaterMapper {

    public Theater toEntity(TheaterRequestDTO dto) {
        if (dto == null) return null;
        Theater theater = new Theater();
        theater.setName(dto.getName());
        theater.setSeats(dto.getSeats());
        if (dto.getLocation() != null) {
            Location location = new Location();
            location.setLatitude(dto.getLocation().getLatitude());
            location.setLongitude(dto.getLocation().getLongitude());
            theater.setLocation(location);
        }
        return theater;
    }

    public TheaterResponseDTO toDTO(Theater theater) {
        if (theater == null) return null;
        TheaterResponseDTO dto = new TheaterResponseDTO();
        dto.setName(theater.getName());
        dto.setSeats(theater.getSeats());
        return dto;
    }
}