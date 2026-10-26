package mx.unam.fciencias.cinema_ciencias.components;

import org.springframework.stereotype.Component;
import mx.unam.fciencias.cinema_ciencias.entities.Ticket;
import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.dto.TicketRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TicketResponseDTO;

@Component
public class TicketMapper {

    public Ticket toEntity(TicketRequestDTO dto, MovieScreening movieScreening) {
        if (dto == null) {
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setMovieScreening(movieScreening);
        ticket.setUserId(dto.getUserId());

        return ticket;
    }

    public TicketResponseDTO toDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setUserId(ticket.getUserId());

        if (ticket.getMovieScreening() != null) {
            dto.setMovieScreeningId(ticket.getMovieScreening().getId());
        }

        return dto;
    }

    public Ticket updateEntityFromDTO(TicketRequestDTO dto, Ticket ticket, MovieScreening movieScreening) {
        if (dto == null || ticket == null) {
            return ticket;
        }

        if (movieScreening != null) {
            ticket.setMovieScreening(movieScreening);
        }
        if (dto.getUserId() != null) {
            ticket.setUserId(dto.getUserId());
        }

        return ticket;
    }
}
