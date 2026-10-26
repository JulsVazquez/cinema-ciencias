package mx.unam.fciencias.cinema_ciencias.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MovieScreeningRequestDTOTest {

    private final LocalDateTime schedule = LocalDateTime.of(2026, 9, 13, 20, 0);

    @Test
    void allArgsConstructorSetsFields() {
        TheaterRequestDTO theater = new TheaterRequestDTO();
        theater.setName("Sala 1");
        theater.setSeats(120);

        MovieScreeningRequestDTO dto = new MovieScreeningRequestDTO(theater, "Avatar", schedule);

        assertThat(dto.getTheater()).isSameAs(theater);
        assertThat(dto.getMovie()).isEqualTo("Avatar");
        assertThat(dto.getSchedule()).isEqualTo(schedule);
    }

    @Test
    void gettersAndSettersWork() {
        MovieScreeningRequestDTO dto = new MovieScreeningRequestDTO();
        TheaterRequestDTO theater = new TheaterRequestDTO();

        dto.setTheater(theater);
        dto.setMovie("Dune: Part Two");
        dto.setSchedule(schedule);

        assertThat(dto.getTheater()).isSameAs(theater);
        assertThat(dto.getMovie()).isEqualTo("Dune: Part Two");
        assertThat(dto.getSchedule()).isEqualTo(schedule);
    }
}