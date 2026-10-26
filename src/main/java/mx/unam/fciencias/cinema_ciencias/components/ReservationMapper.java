package mx.unam.fciencias.cinema_ciencias.components;

import mx.unam.fciencias.cinema_ciencias.dto.ReservationRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.ReservationResponseDTO;
import mx.unam.fciencias.cinema_ciencias.entities.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponseDTO toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationResponseDTO dto = new ReservationResponseDTO();

        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUserId());
        dto.setSeatNumber(reservation.getSeatNumber());
        dto.setReservationTime(reservation.getReservationTime());

        if (reservation.getMovieScreening() != null) {
            dto.setMovieScreeningId(reservation.getMovieScreening().getId());

            if (reservation.getMovieScreening().getMovie() != null) {
                dto.setMovieTitle(reservation.getMovieScreening().getMovie());
            } else {
                dto.setMovieTitle("Título no disponible");
            }
        }

        return dto;
    }

    public Reservation toEntity(ReservationRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(dto.getUserId());
        reservation.setSeatNumber(dto.getSeatNumber());

        return reservation;
    }
}