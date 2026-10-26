package mx.unam.fciencias.cinema_ciencias.services;

import jakarta.persistence.EntityNotFoundException;
import mx.unam.fciencias.cinema_ciencias.dto.LocationRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterRequestDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Location;
import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import mx.unam.fciencias.cinema_ciencias.repositories.MovieScreeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieScreeningServiceTest {

    @Mock
    private MovieScreeningRepository movieScreeningRepository;

    @Mock
    private TheaterService theaterService;

    private MovieScreeningService service;

    private final LocalDateTime baseSchedule = LocalDateTime.of(2026, 9, 13, 20, 0);

    @BeforeEach
    void setUp() {
        service = new MovieScreeningService(movieScreeningRepository, theaterService);
    }

    @Test
    void getAvailableMovieScreeningsDelegatesToRepository() {
        MovieScreening movieScreening = new MovieScreening();
        when(movieScreeningRepository.findAll()).thenReturn(List.of(movieScreening));

        List<MovieScreening> result = service.getAvailableMovieScreenings();

        assertThat(result).containsExactly(movieScreening);
    }

    @Test
    void getMovieScreeningsByMovieDelegatesToRepository() {
        MovieScreening movieScreening = new MovieScreening();
        when(movieScreeningRepository.findByMovie("Avatar")).thenReturn(List.of(movieScreening));

        List<MovieScreening> result = service.getMovieScreeningsByMovie("Avatar");

        assertThat(result).containsExactly(movieScreening);
    }

    @Test
    void getMovieScreeningByIdReturnsMovieScreeningWhenFound() {
        MovieScreening movieScreening = new MovieScreening();
        movieScreening.setId(10L);
        when(movieScreeningRepository.findById(10L)).thenReturn(Optional.of(movieScreening));

        MovieScreening result = service.getMovieScreeningById(10L);

        assertThat(result).isSameAs(movieScreening);
    }

    @Test
    void getMovieScreeningByIdThrowsWhenNotFound() {
        when(movieScreeningRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getMovieScreeningById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void createMovieScreeningSavesWhenNoTimeConflict() {
        Location location = new Location(1L, 19.32, -99.18, new ArrayList<>());
        Theater theater = new Theater(3L, "Sala 1", location, 120);
        when(theaterService.createTheater(any(TheaterRequestDTO.class))).thenReturn(theater);
        when(movieScreeningRepository.findByTheaterNameAndScheduleBetween(anyString(), any(), any()))
                .thenReturn(List.of());

        MovieScreeningRequestDTO request = buildRequest("Avatar", baseSchedule);
        when(movieScreeningRepository.save(any(MovieScreening.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MovieScreening result = service.createMovieScreening(request);

        assertThat(result.getTheater()).isSameAs(theater);
        assertThat(result.getMovie()).isEqualTo("Avatar");
        assertThat(result.getSchedule()).isEqualTo(baseSchedule);
        verify(movieScreeningRepository).save(result);
    }

    @Test
    void createMovieScreeningThrowsWhenOverlappingScreeningExists() {
        Location location = new Location(1L, 19.32, -99.18, new ArrayList<>());
        Theater theater = new Theater(3L, "Sala 1", location, 120);
        when(theaterService.createTheater(any(TheaterRequestDTO.class))).thenReturn(theater);

        // Misma sala, misma location y mismo horario -> conflicto duro
        MovieScreening existing = new MovieScreening(theater, "Avatar 2", baseSchedule);
        when(movieScreeningRepository.findByTheaterNameAndScheduleBetween(anyString(), any(), any()))
                .thenReturn(List.of(existing));

        MovieScreeningRequestDTO request = buildRequest("Avatar", baseSchedule);

        assertThatThrownBy(() -> service.createMovieScreening(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Avatar 2");

        verify(movieScreeningRepository, never()).save(any(MovieScreening.class));
    }

    private MovieScreeningRequestDTO buildRequest(String movie, LocalDateTime schedule) {
        LocationRequestDTO locationRequest = new LocationRequestDTO();
        locationRequest.setLatitude(19.32);
        locationRequest.setLongitude(-99.18);

        TheaterRequestDTO theaterRequest = new TheaterRequestDTO();
        theaterRequest.setName("Sala 1");
        theaterRequest.setLocation(locationRequest);
        theaterRequest.setSeats(120);

        return new MovieScreeningRequestDTO(theaterRequest, movie, schedule);
    }
}