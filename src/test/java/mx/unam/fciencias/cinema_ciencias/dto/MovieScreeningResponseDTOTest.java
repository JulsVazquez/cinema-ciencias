package mx.unam.fciencias.cinema_ciencias.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MovieScreeningResponseDTOTest {

    private final LocalDateTime schedule = LocalDateTime.of(2026, 9, 13, 20, 0);

    @Test
    void allArgsConstructorSetsFields() {
        TheaterResponseDTO theater = new TheaterResponseDTO("Sala 1", 120);

        MovieScreeningResponseDTO dto = new MovieScreeningResponseDTO(15L, theater, "Avatar", schedule);

        assertThat(dto.getId()).isEqualTo(15L);
        assertThat(dto.getTheater()).isSameAs(theater);
        assertThat(dto.getMovie()).isEqualTo("Avatar");
        assertThat(dto.getSchedule()).isEqualTo(schedule);
    }

    @Test
    void gettersAndSettersWork() {
        MovieScreeningResponseDTO dto = new MovieScreeningResponseDTO();
        TheaterResponseDTO theater = new TheaterResponseDTO();

        dto.setId(16L);
        dto.setTheater(theater);
        dto.setMovie("Dune: Part Two");
        dto.setSchedule(schedule);

        assertThat(dto.getId()).isEqualTo(16L);
        assertThat(dto.getTheater()).isSameAs(theater);
        assertThat(dto.getMovie()).isEqualTo("Dune: Part Two");
        assertThat(dto.getSchedule()).isEqualTo(schedule);
    }
}