package ru.geekbrains.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "telegram_bot")
public class User {

    public User() {
    }

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User user = (User) obj;
        return Objects.equals(id, user.getId());
    }


}
