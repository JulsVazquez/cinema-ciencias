package mx.unam.fciencias.cinema_ciencias.events;

import mx.unam.fciencias.cinema_ciencias.entities.Ticket;

import java.io.Serializable;

public class TicketEvent implements Serializable {
    private Long ticketId;
    private Long usuarioId;

    public TicketEvent(){}

    public TicketEvent(Ticket ticket) {
        this.ticketId = ticket.getId();
        this.usuarioId = ticket.getUserId();
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }
}
