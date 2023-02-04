package ru.geekbrains.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.dto.TaskDto;
import ru.geekbrains.entity.Task;
import ru.geekbrains.entity.User;
import ru.geekbrains.repository.TaskRepository;
import ru.geekbrains.repository.UserRepository;

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


}
