package ru.geekbrains.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks", schema = "telegram_bot")
@Data
@NoArgsConstructor
public class Task {
    public Task(String number, User user, LocalDateTime time) {
        this.number = number;
        this.user = user;
        this.time = time;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "started_at")
    private LocalDateTime time;




}
