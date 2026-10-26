package mx.unam.fciencias.cinema_ciencias.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class TheaterTest {

    @Test
    void defaultConstructorCreatesEmptyTheater() {
        Theater theater = new Theater();

        assertThat(theater).isNotNull();
        assertThat(theater.getId()).isNull();
        assertThat(theater.getName()).isNull();
        assertThat(theater.getLocation()).isNull();
        assertThat(theater.getSeats()).isNull();
    }

    @Test
    void allArgsConstructorSetsFields() {
        Location location = new Location(1L, 19.32, -99.18, new ArrayList<>());

        Theater theater = new Theater(7L, "Sala 3", location, 150);

        assertThat(theater.getId()).isEqualTo(7L);
        assertThat(theater.getName()).isEqualTo("Sala 3");
        assertThat(theater.getLocation()).isSameAs(location);
        assertThat(theater.getSeats()).isEqualTo(150);
    }

    @Test
    void gettersAndSettersWork() {
        Theater theater = new Theater();
        Location location = new Location();

        theater.setId(9L);
        theater.setName("Auditorio");
        theater.setLocation(location);
        theater.setSeats(200);

        assertThat(theater.getId()).isEqualTo(9L);
        assertThat(theater.getName()).isEqualTo("Auditorio");
        assertThat(theater.getLocation()).isSameAs(location);
        assertThat(theater.getSeats()).isEqualTo(200);
    }
}