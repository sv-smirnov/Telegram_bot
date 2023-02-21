package ru.geekbrains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.geekbrains.entity.Task;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select t from Task t where t.startedAt = :date and t.user.id = :user_id")
    List<Task> history(LocalDate date, Long user_id);
    //TODO historyBetween

    @Query("select t from Task t" +
            " where t.startedAt = :date group by t.id, t.user.id order by t.user.id")
    List<Task> teamActivityForDate(LocalDate date);
}
