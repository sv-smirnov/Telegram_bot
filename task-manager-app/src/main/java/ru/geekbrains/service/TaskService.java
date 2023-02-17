package ru.geekbrains.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.dto.TaskDto;
import ru.geekbrains.entity.Task;
import ru.geekbrains.entity.User;
import ru.geekbrains.repository.TaskRepository;
import ru.geekbrains.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public void createNewTask(TaskDto taskDto) {
        User user = createUser(taskDto);
        Task task = new Task(taskDto.getTask(), user, taskDto.getTime(), taskDto.getDate());
        taskRepository.save(task);
    }

    private User createUser(TaskDto taskDto) {
        User user = new User(taskDto.getUserId(), taskDto.getUserName(), taskDto.getLastName());
        if (!userRepository.existsById(taskDto.getUserId())) {
            userRepository.save(user);
        }
        return user;
    }

    public List<Task> history(LocalDate date) {
        List<Task> history = taskRepository.history(date);
        System.out.println(history);
        return history;
    }

    //TODO historyBetween


}
