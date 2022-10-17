package ru.practicum.service.compilation;

import lombok.*;
import ru.practicum.service.event.Event;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 4000, nullable = false)
    private String title;

    @Column(name = "pinned")
    private boolean pinned = false;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "events_compilation", joinColumns = @JoinColumn(name = "compilation_id"))
    @Column(name = "event")
    private List<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Compilation that = (Compilation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
