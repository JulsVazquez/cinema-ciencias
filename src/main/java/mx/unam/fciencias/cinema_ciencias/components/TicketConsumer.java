package mx.unam.fciencias.cinema_ciencias.components;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import mx.unam.fciencias.cinema_ciencias.events.TicketEvent;

@Component
public class TicketConsumer {

    @KafkaListener(
            topics = "created-tickets",
            containerFactory = "kafkaListenerContainerFactory"
    )

    public void getCreatedTicket(TicketEvent event) {
        System.out.println("Received & Created Ticket ID: " + event.getTicketId());
        System.out.println("User ID created the ticket: " + event.getUsuarioId());
    }
}