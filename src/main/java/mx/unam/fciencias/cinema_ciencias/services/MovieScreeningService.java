package mx.unam.fciencias.cinema_ciencias.services;
import jakarta.transaction.Transactional;
import mx.unam.fciencias.cinema_ciencias.entities.Location;
import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import mx.unam.fciencias.cinema_ciencias.repositories.MovieScreeningRepository;
import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningRequestDTO;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.time.LocalDateTime;

@Service
@Transactional
public class MovieScreeningService {

    private final MovieScreeningRepository movieScreeningRepository;
    private final TheaterService theaterService;

    public MovieScreeningService(MovieScreeningRepository movieScreeningRepository,
                                 TheaterService theaterService) {
        this.movieScreeningRepository = movieScreeningRepository;
        this.theaterService = theaterService;
    }

    public MovieScreening createMovieScreening(MovieScreeningRequestDTO movieScreeningRequest) {
        Theater theater = theaterService.createTheater(movieScreeningRequest.getTheater());

        LocalDateTime newSchedule = movieScreeningRequest.getSchedule();
        LocalDateTime newStartTime = newSchedule;
        LocalDateTime newEndTime = newSchedule.plusHours(3);

        List<MovieScreening> conflictingScreenings = findConflictingMovieScreeningsForTheater(
                theater.getName(), theater.getLocation(), newStartTime, newEndTime);

        if (!conflictingScreenings.isEmpty()) {
            MovieScreening conflictingScreening = conflictingScreenings.getFirst();
            throw new IllegalArgumentException(
                    String.format("Ya existe una función programada en la sala '%s' en este horario: %s a las %s",
                            theater.getName(),
                            conflictingScreening.getMovie(),
                            conflictingScreening.getSchedule()));
        }

        MovieScreening movieScreening = new MovieScreening();
        movieScreening.setTheater(theater);
        movieScreening.setMovie(movieScreeningRequest.getMovie());
        movieScreening.setSchedule(newSchedule);
        return movieScreeningRepository.save(movieScreening);
    }

    private List<MovieScreening> findConflictingMovieScreeningsForTheater(String theaterName, Location location,
                                                                          LocalDateTime newStartTime, LocalDateTime newEndTime) {
        List<MovieScreening> potentialConflicts = movieScreeningRepository.findByTheaterNameAndScheduleBetween(
                theaterName, newStartTime.minusHours(3), newEndTime);

        return potentialConflicts.stream()
                .filter(existingScreening ->
                        existingScreening.getTheater().getLocation().equals(location) &&
                                hasTimeConflict(existingScreening, newStartTime, newEndTime))
                .toList();
    }

    private boolean hasTimeConflict(MovieScreening existingScreening, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        LocalDateTime existingStart = existingScreening.getSchedule();
        LocalDateTime existingEnd = existingStart.plusHours(3);

        return (newStartTime.isBefore(existingEnd) && newEndTime.isAfter(existingStart));
    }

    public MovieScreening getMovieScreeningById(Long id) {
        return movieScreeningRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MovieScreening not found"));
    }

    public List<MovieScreening> getMovieScreeningsByMovie(String movie) {
        return movieScreeningRepository.findByMovie(movie);
    }

    public List<MovieScreening> getAvailableMovieScreenings() {
        return movieScreeningRepository.findAll();
    }
}