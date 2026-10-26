package mx.unam.fciencias.cinema_ciencias.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TheaterRequestDTOTest {

    @Test
    void gettersAndSettersWork() {
        TheaterRequestDTO dto = new TheaterRequestDTO();
        LocationRequestDTO location = new LocationRequestDTO();
        location.setLatitude(19.32);
        location.setLongitude(-99.18);

        dto.setName("Sala 1");
        dto.setLocation(location);
        dto.setSeats(120);

        assertThat(dto.getName()).isEqualTo("Sala 1");
        assertThat(dto.getLocation()).isSameAs(location);
        assertThat(dto.getLocation().getLatitude()).isEqualTo(19.32);
        assertThat(dto.getLocation().getLongitude()).isEqualTo(-99.18);
        assertThat(dto.getSeats()).isEqualTo(120);
    }

    @Test
    void defaultConstructorCreatesEmptyDTO() {
        TheaterRequestDTO dto = new TheaterRequestDTO();

        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isNull();
        assertThat(dto.getLocation()).isNull();
        assertThat(dto.getSeats()).isNull();
    }
}