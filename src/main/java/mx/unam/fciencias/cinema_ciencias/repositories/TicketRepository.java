package mx.unam.fciencias.cinema_ciencias.repositories;
import mx.unam.fciencias.cinema_ciencias.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long>{
    List<Ticket> findByMovieScreeningId(Long movieScreeningId);
    List<Ticket> findByUserId(Long userId);
    Ticket save(Ticket ticket);
}