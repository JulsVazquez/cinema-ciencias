package mx.unam.fciencias.cinema_ciencias.repositories;

import mx.unam.fciencias.cinema_ciencias.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByLatitudeAndLongitude(Double latitude, Double longitude);

}