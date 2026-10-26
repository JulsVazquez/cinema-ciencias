package mx.unam.fciencias.cinema_ciencias.services;

import jakarta.transaction.Transactional;
import mx.unam.fciencias.cinema_ciencias.dto.ReservationRequestDTO;
import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.entities.Reservation;
import mx.unam.fciencias.cinema_ciencias.repositories.MovieScreeningRepository;
import mx.unam.fciencias.cinema_ciencias.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MovieScreeningRepository movieScreeningRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              MovieScreeningRepository movieScreeningRepository) {
        this.reservationRepository = reservationRepository;
        this.movieScreeningRepository = movieScreeningRepository;
    }

    @Transactional
    public Reservation createReservation(ReservationRequestDTO request) {

        MovieScreening movieScreening = movieScreeningRepository.findById(request.getMovieScreeningId())
                .orElseThrow(() -> new RuntimeException("No se encontró la función"));

        boolean isSeatOccupied = reservationRepository.existsByMovieScreeningIdAndSeatNumber(
                request.getMovieScreeningId(),
                request.getSeatNumber()
        );

        if (isSeatOccupied) {
            throw new RuntimeException("El asiento " + request.getSeatNumber() + " ya está ocupado.");
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(request.getUserId());
        reservation.setMovieScreening(movieScreening);
        reservation.setSeatNumber(request.getSeatNumber());
        reservation.setReservationTime(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new RuntimeException("No se encontró la reservación");
        }
        reservationRepository.deleteById(reservationId);
    }
}