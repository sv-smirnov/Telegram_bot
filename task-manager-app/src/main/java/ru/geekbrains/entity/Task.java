package ru.geekbrains.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks", schema = "telegram_bot")
public class Task {
    public Task() {
    }

    public Task(String description, User user, String spendTime, LocalDate startedAt) {
        this.description = description;
        this.user = user;
        this.spendTime = spendTime;
        this.startedAt = startedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "spend_time")
    private String spendTime;

    @Column(name = "started_at")
    private LocalDate startedAt;


    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getStartedAt() {
        return startedAt;
    }

    @Override
    public int hashCode() {
        return 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
        return id != null && id.equals(other.getId());
    }

}
