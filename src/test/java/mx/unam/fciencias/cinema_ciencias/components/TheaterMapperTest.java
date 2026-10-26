package mx.unam.fciencias.cinema_ciencias.components;

import mx.unam.fciencias.cinema_ciencias.dto.LocationRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterResponseDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Location;
import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TheaterMapperTest {

    private TheaterMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TheaterMapper();
    }

    @Test
    void toEntityMapsAllFields() {
        TheaterRequestDTO dto = new TheaterRequestDTO();
        dto.setName("Sala 1");
        dto.setSeats(120);
        dto.setLocation(buildLocationDTO(19.32, -99.18));

        Theater theater = mapper.toEntity(dto);

        assertThat(theater.getName()).isEqualTo("Sala 1");
        assertThat(theater.getSeats()).isEqualTo(120);
        assertThat(theater.getLocation()).isNotNull();
        assertThat(theater.getLocation().getLatitude()).isEqualTo(19.32);
        assertThat(theater.getLocation().getLongitude()).isEqualTo(-99.18);
    }

    @Test
    void toEntityWithoutLocationLeavesLocationNull() {
        TheaterRequestDTO dto = new TheaterRequestDTO();
        dto.setName("Auditorio");
        dto.setSeats(200);

        Theater theater = mapper.toEntity(dto);

        assertThat(theater.getName()).isEqualTo("Auditorio");
        assertThat(theater.getSeats()).isEqualTo(200);
        assertThat(theater.getLocation()).isNull();
    }

    @Test
    void toEntityWithNullDTOReturnsNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void toDTOMapsAllFields() {
        Theater theater = new Theater();
        theater.setName("Sala 1");
        theater.setSeats(120);
        theater.setLocation(new Location(1L, 19.32, -99.18, new java.util.ArrayList<>()));

        TheaterResponseDTO dto = mapper.toDTO(theater);

        assertThat(dto.getName()).isEqualTo("Sala 1");
        assertThat(dto.getSeats()).isEqualTo(120);
    }

    @Test
    void toDTOWithNullTheaterReturnsNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }

    private LocationRequestDTO buildLocationDTO(Double lat, Double lng) {
        LocationRequestDTO dto = new LocationRequestDTO();
        dto.setLatitude(lat);
        dto.setLongitude(lng);
        return dto;
    }
}