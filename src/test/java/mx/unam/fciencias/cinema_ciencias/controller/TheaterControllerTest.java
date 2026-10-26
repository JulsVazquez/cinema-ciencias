package mx.unam.fciencias.cinema_ciencias.controller;

import mx.unam.fciencias.cinema_ciencias.components.TheaterMapper;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterResponseDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import mx.unam.fciencias.cinema_ciencias.services.TheaterService;
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
class TheaterControllerTest {

    @Mock
    private TheaterService theaterService;

    @Mock
    private TheaterMapper theaterMapper;

    private MockMvc mockMvc;

    private final TheaterResponseDTO responseDTO = new TheaterResponseDTO("Sala 1", 120);

    @BeforeEach
    void setUp() {
        TheaterController controller = new TheaterController(theaterService, theaterMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createTheaterReturnsCreated() throws Exception {
        Theater theater = new Theater(3L, "Sala 1", null, 120);
        when(theaterService.createTheater(any(TheaterRequestDTO.class))).thenReturn(theater);
        when(theaterMapper.toDTO(theater)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/cinema/theater")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Sala 1", "location": {"latitude": 19.32, "longitude": -99.18}, "seats": 120}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sala 1"))
                .andExpect(jsonPath("$.seats").value(120));
    }

    @Test
    void getAllTheatersReturnsOk() throws Exception {
        Theater theater = new Theater(3L, "Sala 1", null, 120);
        when(theaterService.getAvailableTheaters()).thenReturn(List.of(theater));
        when(theaterMapper.toDTO(theater)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/cinema/theater"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sala 1"));
    }

    @Test
    void getTheaterByIdReturnsOk() throws Exception {
        Theater theater = new Theater(3L, "Sala 1", null, 120);
        when(theaterService.getTheaterById(3L)).thenReturn(theater);
        when(theaterMapper.toDTO(theater)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/cinema/theater/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sala 1"))
                .andExpect(jsonPath("$.seats").value(120));
    }
}