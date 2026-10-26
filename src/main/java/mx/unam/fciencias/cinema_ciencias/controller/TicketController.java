package mx.unam.fciencias.cinema_ciencias.controller;

import mx.unam.fciencias.cinema_ciencias.entities.Ticket;
import mx.unam.fciencias.cinema_ciencias.services.TicketService;
import mx.unam.fciencias.cinema_ciencias.dto.TicketRequestDTO;
import mx.unam.fciencias.cinema_ciencias.dto.TicketResponseDTO;
import mx.unam.fciencias.cinema_ciencias.components.TicketMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cinema")
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    public TicketController(TicketService ticketService,
                            TicketMapper ticketMapper){
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @PostMapping("/ticket")
    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody TicketRequestDTO ticketRequest) {
        Ticket ticket = ticketService.createTicket(ticketRequest);
        TicketResponseDTO response = ticketMapper.toDTO(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketResponseDTO> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        TicketResponseDTO response = ticketMapper.toDTO(ticket);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ticket/movie-screening/{movieScreeningId}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByMovieScreening(@PathVariable Long movieScreeningId) {
        List<Ticket> tickets = ticketService.getTicketsByMovieScreening(movieScreeningId);

        List<TicketResponseDTO> response = tickets.stream()
                .map(ticketMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ticket/user/{userId}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByUser(@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.getTicketsByUser(userId);

        List<TicketResponseDTO> response = tickets.stream()
                .map(ticketMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}