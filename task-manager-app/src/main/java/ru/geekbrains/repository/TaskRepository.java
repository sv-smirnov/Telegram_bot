package ru.geekbrains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
