package ru.geekbrains.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users", schema = "telegram_bot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public User(Long userId, String userName, String lastName) {
        this.id = userId;
        this.userName = userName;
        this.lastName = lastName;
    }

    @Id
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "lastname")
    private String lastName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval=true, fetch = FetchType.LAZY)
    private List<Task> tasks;


}
