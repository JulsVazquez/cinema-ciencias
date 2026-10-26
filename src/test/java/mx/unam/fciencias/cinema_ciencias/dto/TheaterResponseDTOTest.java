package mx.unam.fciencias.cinema_ciencias.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TheaterResponseDTOTest {

    @Test
    void allArgsConstructorSetsFields() {
        TheaterResponseDTO dto = new TheaterResponseDTO("Sala 1", 120);

        assertThat(dto.getName()).isEqualTo("Sala 1");
        assertThat(dto.getSeats()).isEqualTo(120);
    }

    @Test
    void gettersAndSettersWork() {
        TheaterResponseDTO dto = new TheaterResponseDTO();

        dto.setName("Auditorio");
        dto.setSeats(200);

        assertThat(dto.getName()).isEqualTo("Auditorio");
        assertThat(dto.getSeats()).isEqualTo(200);
    }

    @Test
    void implementsSerializable() {
        assertThat(TheaterResponseDTO.class).isAssignableTo(java.io.Serializable.class);
    }
}