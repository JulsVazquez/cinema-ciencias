package mx.unam.fciencias.cinema_ciencias.services;

import mx.unam.fciencias.cinema_ciencias.dto.LocationRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterRequestDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Location;
import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import mx.unam.fciencias.cinema_ciencias.repositories.LocationRepository;
import mx.unam.fciencias.cinema_ciencias.repositories.TheaterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TheaterServiceTest {

    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private LocationRepository locationRepository;

    private TheaterService service;

    @BeforeEach
    void setUp() {
        service = new TheaterService(theaterRepository, locationRepository);
    }

    @Test
    void createTheaterReturnsExistingTheaterForSameNameAndLocation() {
        Theater existing = new Theater(1L, "Sala 1", new Location(), 120);
        when(theaterRepository.findByNameAndLocationLatitudeAndLocationLongitude("Sala 1", 19.32, -99.18))
                .thenReturn(Optional.of(existing));

        Theater result = service.createTheater(buildRequest());

        assertThat(result).isSameAs(existing);
        verify(theaterRepository, never()).save(any(Theater.class));
        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    void createTheaterReusesExistingLocation() {
        Location existingLocation = new Location(5L, 19.32, -99.18, new ArrayList<>());
        when(theaterRepository.findByNameAndLocationLatitudeAndLocationLongitude("Sala 1", 19.32, -99.18))
                .thenReturn(Optional.empty());
        when(locationRepository.findByLatitudeAndLongitude(19.32, -99.18)).thenReturn(existingLocation);
        when(theaterRepository.save(any(Theater.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Theater result = service.createTheater(buildRequest());

        assertThat(result.getName()).isEqualTo("Sala 1");
        assertThat(result.getSeats()).isEqualTo(120);
        assertThat(result.getLocation()).isSameAs(existingLocation);
        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    void createTheaterCreatesNewLocationWhenMissing() {
        when(theaterRepository.findByNameAndLocationLatitudeAndLocationLongitude("Sala 1", 19.32, -99.18))
                .thenReturn(Optional.empty());
        when(locationRepository.findByLatitudeAndLongitude(19.32, -99.18)).thenReturn(null);
        when(locationRepository.save(any(Location.class)))
                .thenAnswer(invocation -> {
                    Location l = invocation.getArgument(0);
                    l.setId(9L);
                    return l;
                });
        when(theaterRepository.save(any(Theater.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Theater result = service.createTheater(buildRequest());

        ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);
        verify(locationRepository).save(locationCaptor.capture());
        assertThat(locationCaptor.getValue().getLatitude()).isEqualTo(19.32);
        assertThat(locationCaptor.getValue().getLongitude()).isEqualTo(-99.18);

        assertThat(result.getLocation()).isNotNull();
        assertThat(result.getLocation().getId()).isEqualTo(9L);
        assertThat(result.getLocation().getLatitude()).isEqualTo(19.32);
        assertThat(result.getLocation().getLongitude()).isEqualTo(-99.18);
    }

    @Test
    void getAvailableTheatersDelegatesToRepository() {
        Theater theater = new Theater();
        when(theaterRepository.findAll()).thenReturn(List.of(theater));

        List<Theater> result = service.getAvailableTheaters();

        assertThat(result).containsExactly(theater);
    }

    @Test
    void getTheaterByIdReturnsTheaterWhenFound() {
        Theater theater = new Theater(7L, "Sala 3", null, 150);
        when(theaterRepository.findTheaterById(7L)).thenReturn(Optional.of(theater));

        Theater result = service.getTheaterById(7L);

        assertThat(result).isSameAs(theater);
    }

    @Test
    void getTheaterByIdThrowsWhenNotFound() {
        when(theaterRepository.findTheaterById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTheaterById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Theater not found 99");
    }

    private TheaterRequestDTO buildRequest() {
        LocationRequestDTO locationRequest = new LocationRequestDTO();
        locationRequest.setLatitude(19.32);
        locationRequest.setLongitude(-99.18);

        TheaterRequestDTO request = new TheaterRequestDTO();
        request.setName("Sala 1");
        request.setLocation(locationRequest);
        request.setSeats(120);
        return request;
    }
}