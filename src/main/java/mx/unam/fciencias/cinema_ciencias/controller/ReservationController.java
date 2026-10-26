package mx.unam.fciencias.cinema_ciencias.controller;

import jakarta.validation.Valid;
import mx.unam.fciencias.cinema_ciencias.components.ReservationMapper;
import mx.unam.fciencias.cinema_ciencias.dto.ReservationRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.ReservationResponseDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Reservation;
import mx.unam.fciencias.cinema_ciencias.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cinema")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    public ReservationController(ReservationService reservationService,
                                 ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    @PostMapping("/reservation")
    public ResponseEntity<ReservationResponseDTO>
            createReservation(@Valid
                             @RequestBody
                             ReservationRequestDTO reservationRequest){
        Reservation reservation = reservationService.createReservation(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationMapper.toDTO(reservation));
    }

    @GetMapping("/reservation/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByUser(@PathVariable Long userId) {
        List<ReservationResponseDTO> response = reservationService.getReservationsByUserId(userId)
                .stream()
                .map(reservationMapper::toDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reservation/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId){
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
