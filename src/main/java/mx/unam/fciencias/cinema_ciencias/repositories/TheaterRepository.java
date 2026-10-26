package mx.unam.fciencias.cinema_ciencias.repositories;

import mx.unam.fciencias.cinema_ciencias.entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Optional<Theater> findByName(String name);
    Optional<Theater> findTheaterById(Long id);
    Optional<Theater> findByNameAndLocationLatitudeAndLocationLongitude(String name, Double latitude, Double longitude);
}