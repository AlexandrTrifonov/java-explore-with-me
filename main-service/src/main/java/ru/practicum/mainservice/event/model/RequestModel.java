package ru.practicum.mainservice.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.mainservice.event.enums.RequestStatus;
import ru.practicum.mainservice.user.model.UserModel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "requests")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    EventModel event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    UserModel requester;
    @Column(nullable = false)
    LocalDateTime created;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RequestStatus status;
}
