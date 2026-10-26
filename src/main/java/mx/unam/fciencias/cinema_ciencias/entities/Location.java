package mx.unam.fciencias.cinema_ciencias.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitude;
    private Double longitude;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Theater> theaters = new ArrayList<>();

    public Location() {
    }

    public Location(Long id, Double latitude, Double longitude, List<Theater> theaters) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.theaters = theaters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Theater> getTheaters() {
        return theaters;
    }

    public void setTheaters(List<Theater> theaters) {
        this.theaters = theaters;
    }

}