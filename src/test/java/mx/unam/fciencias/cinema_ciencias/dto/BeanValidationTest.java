package mx.unam.fciencias.cinema_ciencias.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de Bean Validation para los DTOs de entrada.
 * Requieren un provider en el classpath (hibernate-validator vía spring-boot-starter-validation).
 */
class BeanValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void theaterRequestValidHasNoViolations() {
        TheaterRequestDTO dto = new TheaterRequestDTO();
        dto.setName("Sala 1");
        dto.setLocation(buildLocation());
        dto.setSeats(120);

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void theaterRequestWithBlankNameHasViolation() {
        TheaterRequestDTO dto = new TheaterRequestDTO();
        dto.setName(" ");
        dto.setLocation(buildLocation());
        dto.setSeats(120);

        Set<ConstraintViolation<TheaterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void theaterRequestWithoutSeatsHasViolation() {
        TheaterRequestDTO dto = new TheaterRequestDTO();
        dto.setName("Sala 1");
        dto.setLocation(buildLocation());

        Set<ConstraintViolation<TheaterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("seats"));
    }

    @Test
    void movieScreeningRequestWithoutTheaterHasViolation() {
        MovieScreeningRequestDTO dto = new MovieScreeningRequestDTO(null, "Avatar", LocalDateTime.of(2026, 9, 13, 20, 0));

        Set<ConstraintViolation<MovieScreeningRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("theater"));
    }

    @Test
    void reservationRequestWithInvalidSeatHasViolation() {
        ReservationRequestDTO dto = new ReservationRequestDTO();
        dto.setUserId(1L);
        dto.setMovieScreeningId(1L);
        dto.setSeatNumber(0);

        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("seatNumber"));
    }

    @Test
    void reservationRequestValidHasNoViolations() {
        ReservationRequestDTO dto = new ReservationRequestDTO();
        dto.setUserId(1L);
        dto.setMovieScreeningId(1L);
        dto.setSeatNumber(5);

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void ticketRequestWithoutUserHasViolation() {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setMovieScreeningId(1L);

        Set<ConstraintViolation<TicketRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("userId"));
    }

    @Test
    void ticketRequestValidHasNoViolations() {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setMovieScreeningId(1L);
        dto.setUserId(2L);

        assertThat(validator.validate(dto)).isEmpty();
    }

    private LocationRequestDTO buildLocation() {
        LocationRequestDTO location = new LocationRequestDTO();
        location.setLatitude(19.32);
        location.setLongitude(-99.18);
        return location;
    }
}