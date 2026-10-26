package mx.unam.fciencias.cinema_ciencias.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_screening_id")
    private MovieScreening movieScreening;

    // El usuario vive en el Auth Service: aquí solo se guarda su identificador.
    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Ticket() {}

    public Ticket(MovieScreening movieScreening, Long userId) {
        this.movieScreening = movieScreening;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieScreening getMovieScreening() {
        return movieScreening;
    }

    public void setMovieScreening(MovieScreening movieScreening) {
        this.movieScreening = movieScreening;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", movieScreening=" + (movieScreening != null ? movieScreening.getId() : "null") +
                ", userId=" + userId +
                '}';
    }
}
