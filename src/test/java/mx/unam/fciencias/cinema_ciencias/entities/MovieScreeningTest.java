package mx.unam.fciencias.cinema_ciencias.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class MovieScreeningTest {

    private LocalDateTime schedule = LocalDateTime.of(2026, 9, 13, 20, 0);

    @Test
    void defaultConstructorCreatesEmptyMovieScreening() {
        MovieScreening movieScreening = new MovieScreening();

        assertThat(movieScreening).isNotNull();
        assertThat(movieScreening.getId()).isNull();
        assertThat(movieScreening.getTheater()).isNull();
        assertThat(movieScreening.getMovie()).isNull();
        assertThat(movieScreening.getSchedule()).isNull();
    }

    @Test
    void allArgsConstructorSetsFields() {
        Theater theater = new Theater(1L, "Sala 1", new Location(1L, 19.32, -99.18, new ArrayList<>()), 120);

        MovieScreening movieScreening = new MovieScreening(theater, "Avatar", schedule);

        assertThat(movieScreening.getTheater()).isSameAs(theater);
        assertThat(movieScreening.getMovie()).isEqualTo("Avatar");
        assertThat(movieScreening.getSchedule()).isEqualTo(schedule);
    }

    @Test
    void gettersAndSettersWork() {
        MovieScreening movieScreening = new MovieScreening();

        movieScreening.setId(42L);
        movieScreening.setSchedule(schedule);
        movieScreening.setMovie("Dune: Part Two");

        assertThat(movieScreening.getId()).isEqualTo(42L);
        assertThat(movieScreening.getSchedule()).isEqualTo(schedule);
        assertThat(movieScreening.getMovie()).isEqualTo("Dune: Part Two");

        Theater theater = new Theater();
        movieScreening.setTheater(theater);
        assertThat(movieScreening.getTheater()).isSameAs(theater);
    }
}