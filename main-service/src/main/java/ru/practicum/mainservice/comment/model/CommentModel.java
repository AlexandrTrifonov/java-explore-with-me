package ru.practicum.mainservice.comment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.mainservice.constants.Constants;
import ru.practicum.mainservice.event.model.EventModel;
import ru.practicum.mainservice.user.model.UserModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Table(name = "comments")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_COMMENT_TEXT, max = Constants.MAX_LENGTH_COMMENT_TEXT)
    String commentText;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    UserModel author;
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    EventModel event;
}
