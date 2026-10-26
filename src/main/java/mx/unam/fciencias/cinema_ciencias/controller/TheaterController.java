package mx.unam.fciencias.cinema_ciencias.controller;

import jakarta.validation.Valid;
import mx.unam.fciencias.cinema_ciencias.components.TheaterMapper;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TheaterResponseDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import mx.unam.fciencias.cinema_ciencias.services.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cinema")
public class TheaterController {
    private final TheaterService theaterService;
    private final TheaterMapper theaterMapper;

    public TheaterController(TheaterService theaterService, TheaterMapper theaterMapper) {
        this.theaterService = theaterService;
        this.theaterMapper = theaterMapper;
    }

    @PostMapping("/theater")
    public ResponseEntity<TheaterResponseDTO> createTheater(@Valid @RequestBody TheaterRequestDTO theaterRequest) {
        Theater theater = theaterService.createTheater(theaterRequest);
        TheaterResponseDTO response = theaterMapper.toDTO(theater);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/theater")
    public ResponseEntity<List<TheaterResponseDTO>> getAllTheaters() {
        List<Theater> theaters = theaterService.getAvailableTheaters();
        List<TheaterResponseDTO> response = theaters.stream()
                .map(theaterMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/theater/{id}")
    public ResponseEntity<TheaterResponseDTO> getTheater(
            @PathVariable Long id){
        Theater theater = theaterService.getTheaterById(id);
        TheaterResponseDTO response = theaterMapper.toDTO(theater);
        return ResponseEntity.ok(response);
    }
}