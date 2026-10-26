package mx.unam.fciencias.cinema_ciencias.components;

import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import org.springframework.stereotype.Component;
import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningResponseDTO;

@Component
public class MovieScreeningMapper {

    private final TheaterMapper theaterMapper;

    public MovieScreeningMapper(TheaterMapper theaterMapper) {
        this.theaterMapper = theaterMapper;
    }

    public MovieScreening toEntity(MovieScreeningRequestDTO dto) {
        MovieScreening movieScreening = new MovieScreening();
        Theater theater = theaterMapper.toEntity(dto.getTheater());
        movieScreening.setTheater(theater);
        movieScreening.setMovie(dto.getMovie());
        movieScreening.setSchedule(dto.getSchedule());
        return movieScreening;
    }

    public MovieScreeningResponseDTO toDTO(MovieScreening movieScreening) {
        MovieScreeningResponseDTO dto = new MovieScreeningResponseDTO();
        dto.setId(movieScreening.getId());
        dto.setTheater(theaterMapper.toDTO(movieScreening.getTheater()));
        dto.setMovie(movieScreening.getMovie());
        dto.setSchedule(movieScreening.getSchedule());
        return dto;
    }
}