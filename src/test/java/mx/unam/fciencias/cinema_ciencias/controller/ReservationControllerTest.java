package mx.unam.fciencias.cinema_ciencias.controller;

import mx.unam.fciencias.cinema_ciencias.components.ReservationMapper;
import mx.unam.fciencias.cinema_ciencias.dto.ReservationRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.ReservationResponseDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Reservation;
import mx.unam.fciencias.cinema_ciencias.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationMapper reservationMapper;

    private MockMvc mockMvc;

    private final Reservation reservation = new Reservation();
    private final ReservationResponseDTO responseDTO = new ReservationResponseDTO();

    @BeforeEach
    void setUp() {
        reservation.setId(5L);
        responseDTO.setId(5L);
        responseDTO.setUserId(2L);
        responseDTO.setMovieScreeningId(15L);
        responseDTO.setSeatNumber(4);
        ReservationController controller = new ReservationController(reservationService, reservationMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createReservationReturnsCreated() throws Exception {
        when(reservationService.createReservation(any(ReservationRequestDTO.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(reservation)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/cinema/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId": 2, "movieScreeningId": 15, "seatNumber": 4}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.seatNumber").value(4));
    }

    @Test
    void createReservationWithInvalidSeatReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/cinema/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId": 2, "movieScreeningId": 15, "seatNumber": 0}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReservationsByUserReturnsOk() throws Exception {
        when(reservationService.getReservationsByUserId(2L)).thenReturn(List.of(reservation));
        when(reservationMapper.toDTO(reservation)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/cinema/reservation/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5));
    }

    @Test
    void cancelReservationReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/cinema/reservation/5"))
                .andExpect(status().isNoContent());

        verify(reservationService).cancelReservation(5L);
    }
}