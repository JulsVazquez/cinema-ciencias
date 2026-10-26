package mx.unam.fciencias.cinema_ciencias.controller;

import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.services.MovieScreeningService;
import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningResponseDTO;
import mx.unam.fciencias.cinema_ciencias.dto.MovieScreeningRequestDTO;
import mx.unam.fciencias.cinema_ciencias.components.MovieScreeningMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cinema")
public class MovieScreeningController {

    private final MovieScreeningService movieScreeningService;
    private final MovieScreeningMapper movieScreeningMapper;

    public MovieScreeningController(MovieScreeningService movieScreeningService,
                                    MovieScreeningMapper movieScreeningMapper){
        this.movieScreeningService = movieScreeningService;
        this.movieScreeningMapper = movieScreeningMapper;
    }

    @PostMapping("/movie-screening")
    public ResponseEntity<MovieScreeningResponseDTO> createMovieScreening(@Valid @RequestBody MovieScreeningRequestDTO movieScreeningRequest) {
        MovieScreening movieScreening = movieScreeningService.createMovieScreening(movieScreeningRequest);
        MovieScreeningResponseDTO response = movieScreeningMapper.toDTO(movieScreening);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/movie-screening")
    public ResponseEntity<List<MovieScreeningResponseDTO>> getAllMovieScreenings() {
        List<MovieScreening> movieScreenings = movieScreeningService.getAvailableMovieScreenings();
        List<MovieScreeningResponseDTO> response = movieScreenings.stream()
                .map(movieScreeningMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movie-screening/{id}")
    public ResponseEntity<MovieScreeningResponseDTO> getMovieScreening(@PathVariable Long id) {
        MovieScreening movieScreening = movieScreeningService.getMovieScreeningById(id);
        MovieScreeningResponseDTO response = movieScreeningMapper.toDTO(movieScreening);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movie-screening/movie/{movieName}")
    public ResponseEntity<List<MovieScreeningResponseDTO>> getMovieScreeningsByMovie(@PathVariable String movieName) {
        List<MovieScreening> movieScreenings = movieScreeningService.getMovieScreeningsByMovie(movieName);
        List<MovieScreeningResponseDTO> response = movieScreenings.stream()
                .map(movieScreeningMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}