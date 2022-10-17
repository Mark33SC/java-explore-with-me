package ru.practicum.service.comment;

import lombok.*;
import ru.practicum.service.event.Event;
import ru.practicum.service.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "text", length = 5000)
    String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "created_on")
    @Builder.Default
    LocalDateTime createdOn = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
