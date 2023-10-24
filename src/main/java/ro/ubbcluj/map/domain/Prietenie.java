package ro.ubbcluj.map.domain;

import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long, Long>> {

    LocalDateTime date;

    public Prietenie(Long id1, Long id2) {
        this.setId(new Tuple<>(id1, id2));
        this.date = LocalDateTime.now();
    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }
}
