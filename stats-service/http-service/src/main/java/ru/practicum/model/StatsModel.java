package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "stats")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String app;
    String uri;
    String ip;
    @Column(name = "time_stamp")
    LocalDateTime timestamp;
}
