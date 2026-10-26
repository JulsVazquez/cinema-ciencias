package mx.unam.fciencias.cinema_ciencias.repositories;

import mx.unam.fciencias.cinema_ciencias.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Recupera una lista de reservaciones basadas en el ID del usuario.
     * Utilizado en ReservationService.getReservationsByUserId(Long userId).
     *
     * @param userId El identificador del usuario.
     * @return Lista de reservaciones asociadas al usuario.
     */
    List<Reservation> findByUserId(Long userId);
    boolean existsByMovieScreeningIdAndSeatNumber(Long movieScreeningId, Integer seatNumber);
}