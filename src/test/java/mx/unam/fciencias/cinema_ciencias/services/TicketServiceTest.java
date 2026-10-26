package mx.unam.fciencias.cinema_ciencias.services;

import jakarta.persistence.EntityNotFoundException;
import mx.unam.fciencias.cinema_ciencias.client.AuditClient;
import mx.unam.fciencias.cinema_ciencias.dto.TicketRequestDTO;
import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import mx.unam.fciencias.cinema_ciencias.entities.Ticket;
import mx.unam.fciencias.cinema_ciencias.events.TicketEvent;
import mx.unam.fciencias.cinema_ciencias.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private MovieScreeningService movieScreeningService;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private AuditClient auditClient;

    private TicketService service;

    @BeforeEach
    void setUp() {
        service = new TicketService(ticketRepository, movieScreeningService, kafkaTemplate, auditClient);
    }

    @Test
    void createTicketStoresUserIdPublishesEventAndAudits() {
        MovieScreening movieScreening = new MovieScreening();
        movieScreening.setId(15L);
        TicketRequestDTO request = new TicketRequestDTO();
        request.setMovieScreeningId(15L);
        request.setUserId(7L);
        when(movieScreeningService.getMovieScreeningById(15L)).thenReturn(movieScreening);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        Ticket result = service.createTicket(request);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getUserId()).isEqualTo(7L);
        assertThat(result.getMovieScreening()).isSameAs(movieScreening);

        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(kafkaTemplate).send(eq("created-tickets"), eventCaptor.capture());
        TicketEvent event = (TicketEvent) eventCaptor.getValue();
        assertThat(event.getTicketId()).isEqualTo(10L);
        assertThat(event.getUsuarioId()).isEqualTo(7L);

        verify(auditClient).sendLog(eq(7L), eq("TICKET_CREATED"), any(String.class));
    }

    @Test
    void getTicketsByUserDelegatesToRepository() {
        Ticket ticket = new Ticket(new MovieScreening(), 7L);
        when(ticketRepository.findByUserId(7L)).thenReturn(List.of(ticket));

        assertThat(service.getTicketsByUser(7L)).containsExactly(ticket);
    }

    @Test
    void getTicketByIdThrowsWhenMissing() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTicketById(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
