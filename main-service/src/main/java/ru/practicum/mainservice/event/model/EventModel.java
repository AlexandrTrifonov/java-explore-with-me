package ru.practicum.mainservice.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.mainservice.category.model.CategoryModel;
import ru.practicum.mainservice.constants.Constants;
import ru.practicum.mainservice.event.enums.State;
import ru.practicum.mainservice.user.model.UserModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Table(name = "events")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_ANNOTATION, max = Constants.MAX_LENGTH_ANNOTATION)
    String annotation;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    CategoryModel categoryModel;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(nullable = false)
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_DESCRIPTION, max = Constants.MAX_LENGTH_DESCRIPTION)
    String description;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    UserModel initiator;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    LocationModel location;
    @Column(nullable = false)
    Boolean paid;
    @Column(name = "participant_limit")
    Long participantLimit;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @Column(nullable = false)
    @Size(min = Constants.MIN_LENGTH_TITLE, max = Constants.MAX_LENGTH_TITLE)
    String title;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    State state;
}
