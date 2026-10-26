package mx.unam.fciencias.cinema_ciencias.services;

import jakarta.transaction.Transactional;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterRequestDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import mx.unam.fciencias.cinema_ciencias.entities.Location;
import mx.unam.fciencias.cinema_ciencias.repositories.LocationRepository;
import mx.unam.fciencias.cinema_ciencias.repositories.TheaterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TheaterService {
    private final TheaterRepository theaterRepository;
    private final LocationRepository locationRepository;

    public TheaterService(TheaterRepository theaterRepository,
                          LocationRepository locationRepository) {
        this.theaterRepository = theaterRepository;
        this.locationRepository = locationRepository;
    }

    public Theater createTheater(TheaterRequestDTO theaterRequest) {
        Double latitude = theaterRequest.getLocation().getLatitude();
        Double longitude = theaterRequest.getLocation().getLongitude();

        // Buscar si ya existe una sala con el MISMO NOMBRE Y MISMA UBICACIÓN
        Optional<Theater> existing = theaterRepository.findByNameAndLocationLatitudeAndLocationLongitude(
                theaterRequest.getName(), latitude, longitude);

        if (existing.isPresent()) {
            return existing.get();
        }

        Location location = locationRepository.findByLatitudeAndLongitude(latitude, longitude);
        if (location == null) {
            Location newLocation = new Location();
            newLocation.setLatitude(latitude);
            newLocation.setLongitude(longitude);
            location = locationRepository.save(newLocation);
        }

        Theater theater = new Theater();
        theater.setName(theaterRequest.getName());
        theater.setLocation(location);
        theater.setSeats(theaterRequest.getSeats());

        return theaterRepository.save(theater);
    }

    public List<Theater> getAvailableTheaters() {
        return theaterRepository.findAll();
    }

    public Theater getTheaterById(Long id) {
        return theaterRepository
                .findTheaterById(id)
                .orElseThrow(() -> new RuntimeException("Theater not found " + id));
    }
}