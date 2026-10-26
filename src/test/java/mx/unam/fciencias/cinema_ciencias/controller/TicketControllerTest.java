package mx.unam.fciencias.cinema_ciencias.controller;

import mx.unam.fciencias.cinema_ciencias.components.TicketMapper;
import mx.unam.fciencias.cinema_ciencias.dto.TicketRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TicketResponseDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Ticket;
import mx.unam.fciencias.cinema_ciencias.services.TicketService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private TicketMapper ticketMapper;

    private MockMvc mockMvc;

    private final Ticket ticket = new Ticket();
    private final TicketResponseDTO responseDTO = new TicketResponseDTO();

    @BeforeEach
    void setUp() {
        ticket.setId(10L);
        responseDTO.setId(10L);
        responseDTO.setMovieScreeningId(15L);
        responseDTO.setUserId(2L);
        TicketController controller = new TicketController(ticketService, ticketMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createTicketReturnsCreated() throws Exception {
        when(ticketService.createTicket(any(TicketRequestDTO.class))).thenReturn(ticket);
        when(ticketMapper.toDTO(ticket)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/cinema/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"movieScreeningId": 15, "userId": 2}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.movieScreeningId").value(15));
    }

    @Test
    void getTicketByIdReturnsOk() throws Exception {
        when(ticketService.getTicketById(10L)).thenReturn(ticket);
        when(ticketMapper.toDTO(ticket)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/cinema/ticket/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void getTicketsByMovieScreeningReturnsOk() throws Exception {
        when(ticketService.getTicketsByMovieScreening(15L)).thenReturn(List.of(ticket));
        when(ticketMapper.toDTO(ticket)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/cinema/ticket/movie-screening/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieScreeningId").value(15));
    }

    @Test
    void getTicketsByUserReturnsOk() throws Exception {
        when(ticketService.getTicketsByUser(2L)).thenReturn(List.of(ticket));
        when(ticketMapper.toDTO(ticket)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/cinema/ticket/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(2));
    }
}