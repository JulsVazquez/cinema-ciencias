package mx.unam.fciencias.cinema_ciencias.dto;

public class TicketResponseDTO {
    private Long id;
    private Long movieScreeningId;
    private Long userId;

    public TicketResponseDTO() {}

    public TicketResponseDTO(Long id, Long movieScreeningId, Long userId) {
        this.id = id;
        this.movieScreeningId = movieScreeningId;
        this.userId = userId;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMovieScreeningId() { return movieScreeningId; }
    public void setMovieScreeningId(Long movieScreeningId) { this.movieScreeningId = movieScreeningId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "TicketResponseDTO{" +
                "id=" + id +
                ", movieScreeningId=" + movieScreeningId +
                ", userId=" + userId +
                '}';
    }
}
