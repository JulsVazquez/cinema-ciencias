package mx.unam.fciencias.cinema_ciencias.services;
import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.entities.Ticket;
import mx.unam.fciencias.cinema_ciencias.events.TicketEvent;
import mx.unam.fciencias.cinema_ciencias.repositories.TicketRepository;
import mx.unam.fciencias.cinema_ciencias.dto.TicketRequestDTO;
import mx.unam.fciencias.cinema_ciencias.client.AuditClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final MovieScreeningService movieScreeningService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AuditClient auditClient;

    public TicketService(TicketRepository ticketRepository,
                         MovieScreeningService movieScreeningService,
                         KafkaTemplate<String, Object> kafkaTemplate,
                         AuditClient auditClient) {
        this.ticketRepository = ticketRepository;
        this.movieScreeningService = movieScreeningService;
        this.kafkaTemplate = kafkaTemplate;
        this.auditClient = auditClient;
    }

    public Ticket createTicket(TicketRequestDTO ticketRequest) {
        MovieScreening movieScreening = movieScreeningService.getMovieScreeningById(ticketRequest.getMovieScreeningId());
        Ticket ticket = new Ticket();
        ticket.setMovieScreening(movieScreening);
        // El usuario ya fue autenticado por el Gateway; el Auth Service es el dueño de la tabla users.
        ticket.setUserId(ticketRequest.getUserId());
        Ticket savedTicket = ticketRepository.save(ticket);
        TicketEvent event = new TicketEvent(savedTicket);
        kafkaTemplate.send("created-tickets", event);
        auditClient.sendLog(savedTicket.getUserId(), "TICKET_CREATED", "Se vendió el ticket " +
                             savedTicket.getId() + " para la función " +
                             movieScreening.getId());
        return savedTicket;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByMovieScreening(Long movieScreeningId) {
        return ticketRepository.findByMovieScreeningId(movieScreeningId);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByUser(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
    }
}
