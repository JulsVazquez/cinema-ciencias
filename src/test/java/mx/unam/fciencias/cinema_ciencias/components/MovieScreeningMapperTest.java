package mx.unam.fciencias.cinema_ciencias.components;

import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningResponseDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterResponseDTO;
import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieScreeningMapperTest {

    @Mock
    private TheaterMapper theaterMapper;

    private MovieScreeningMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new MovieScreeningMapper(theaterMapper);
    }

    @Test
    void toEntityMapsAllFields() {
        TheaterRequestDTO theaterRequest = new TheaterRequestDTO();
        theaterRequest.setName("Sala 1");
        theaterRequest.setSeats(120);
        MovieScreeningRequestDTO request =
                new MovieScreeningRequestDTO(theaterRequest, "Avatar", LocalDateTime.of(2026, 9, 13, 20, 0));

        Theater theater = new Theater();
        theater.setName("Sala 1");
        theater.setSeats(120);
        when(theaterMapper.toEntity(request.getTheater())).thenReturn(theater);

        MovieScreening movieScreening = mapper.toEntity(request);

        assertThat(movieScreening.getTheater()).isSameAs(theater);
        assertThat(movieScreening.getMovie()).isEqualTo("Avatar");
        assertThat(movieScreening.getSchedule()).isEqualTo(LocalDateTime.of(2026, 9, 13, 20, 0));
    }

    @Test
    void toDTOMapsAllFields() {
        Theater theater = new Theater();
        theater.setId(3L);
        theater.setName("Sala 1");
        theater.setSeats(120);
        MovieScreening movieScreening = new MovieScreening();
        movieScreening.setId(15L);
        movieScreening.setTheater(theater);
        movieScreening.setMovie("Avatar");
        movieScreening.setSchedule(LocalDateTime.of(2026, 9, 13, 20, 0));

        TheaterResponseDTO theaterDTO = new TheaterResponseDTO("Sala 1", 120);
        when(theaterMapper.toDTO(theater)).thenReturn(theaterDTO);

        MovieScreeningResponseDTO dto = mapper.toDTO(movieScreening);

        assertThat(dto.getId()).isEqualTo(15L);
        assertThat(dto.getTheater()).isSameAs(theaterDTO);
        assertThat(dto.getMovie()).isEqualTo("Avatar");
        assertThat(dto.getSchedule()).isEqualTo(LocalDateTime.of(2026, 9, 13, 20, 0));
    }
}