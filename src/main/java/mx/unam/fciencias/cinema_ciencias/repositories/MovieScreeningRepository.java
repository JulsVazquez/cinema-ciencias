package mx.unam.fciencias.cinema_ciencias.repositories;

import mx.unam.fciencias.cinema_ciencias.entities.MovieScreening;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface MovieScreeningRepository extends JpaRepository<MovieScreening, Long>{
    List<MovieScreening> findByMovie(String movie);
    List<MovieScreening> findByTheaterNameAndScheduleBetween(String name, LocalDateTime start, LocalDateTime end);
}