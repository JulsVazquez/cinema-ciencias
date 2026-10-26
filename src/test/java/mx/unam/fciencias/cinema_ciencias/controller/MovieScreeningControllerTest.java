package mx.unam.fciencias.cinema_ciencias.controller;

import mx.unam.fciencias.cinema_ciencias.components.MovieScreeningMapper;
import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningResponseDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterResponseDTO;
import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.services.MovieScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MovieScreeningControllerTest {

    @Mock
    private MovieScreeningService movieScreeningService;

    @Mock
    private MovieScreeningMapper movieScreeningMapper;

    private MockMvc mockMvc;

    private final MovieScreeningResponseDTO responseDTO =
            new MovieScreeningResponseDTO(15L, new TheaterResponseDTO("Sala 1", 120), "Avatar", LocalDateTime.of(2026, 9, 13, 20, 0));

    @BeforeEach
    void setUp() {
        MovieScreeningController controller =
                new MovieScreeningController(movieScreeningService, movieScreeningMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createMovieScreeningReturnsCreated() throws Exception {
        MovieScreening movieScreening = new MovieScreening();
        movieScreening.setId(15L);
        when(movieScreeningService.createMovieScreening(any(MovieScreeningRequestDTO.class))).thenReturn(movieScreening);
        when(movieScreeningMapper.toDTO(movieScreening)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/cinema/movie-screening")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "theater": {"name": "Sala 1", "location": {"latitude": 19.32, "longitude": -99.18}, "seats": 120},
                                  "movie": "Avatar",
                                  "schedule": "2026-09-13T20:00:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.movie").value("Avatar"));
    }

    @Test
    void getAllMovieScreeningsReturnsOk() throws Exception {
        MovieScreening movieScreening = new MovieScreening();
        movieScreening.setId(15L);
        when(movieScreeningService.getAvailableMovieScreenings()).thenReturn(List.of(movieScreening));
        when(movieScreeningMapper.toDTO(movieScreening)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/cinema/movie-screening"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(15))
                .andExpect(jsonPath("$[0].movie").value("Avatar"));
    }

    @Test
    void getMovieScreeningByIdReturnsOk() throws Exception {
        MovieScreening movieScreening = new MovieScreening();
        movieScreening.setId(15L);
        when(movieScreeningService.getMovieScreeningById(15L)).thenReturn(movieScreening);
        when(movieScreeningMapper.toDTO(movieScreening)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/cinema/movie-screening/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(15));
    }

    @Test
    void getMovieScreeningsByMovieReturnsOk() throws Exception {
        MovieScreening movieScreening = new MovieScreening();
        movieScreening.setId(15L);
        when(movieScreeningService.getMovieScreeningsByMovie("Avatar")).thenReturn(List.of(movieScreening));
        when(movieScreeningMapper.toDTO(movieScreening)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/cinema/movie-screening/movie/Avatar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movie").value("Avatar"));
    }
}