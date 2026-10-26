package mx.unam.fciencias.cinema_ciencias.exception;

import jakarta.persistence.EntityNotFoundException;
import mx.unam.fciencias.cinema_ciencias.components.TheaterMapper;
import mx.unam.fciencias.cinema_ciencias.controller.TheaterController;
import mx.unam.fciencias.cinema_ciencias.services.TheaterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica que el GlobalExceptionHandler responda 400/404 directamente
 * (sin pasar por el re-dispatch a /error que Spring Security convierte en 403).
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private TheaterService theaterService;

    @Mock
    private TheaterMapper theaterMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TheaterController controller = new TheaterController(theaterService, theaterMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void invalidBodyReturns400WithFieldErrors() throws Exception {
        mockMvc.perform(post("/api/v1/cinema/theater")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": ""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.location").exists())
                .andExpect(jsonPath("$.fieldErrors.seats").exists());
    }

    @Test
    void unreadableBodyReturns400() throws Exception {
        mockMvc.perform(post("/api/v1/cinema/theater")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ json malformado "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void notFoundEntityReturns404WithMessage() throws Exception {
        when(theaterService.getTheaterById(99L))
                .thenThrow(new EntityNotFoundException("The theater was not found!"));

        mockMvc.perform(get("/api/v1/cinema/theater/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("The theater was not found!"));
    }
}
