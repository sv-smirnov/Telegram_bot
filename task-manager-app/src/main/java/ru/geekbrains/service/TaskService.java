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
        User user = checkUser(taskDto);
        Task task = new Task(taskDto.getTask(), user, taskDto.getTime());

        taskRepository.save(task);
    }

    private User checkUser(TaskDto taskDto) {
        User user = new User(taskDto.getUserId(), taskDto.getUserName(), taskDto.getLastName());
        if (userRepository.findById(taskDto.getUserId()).isEmpty()) {
            userRepository.save(user);
            System.out.println("User successfully added");
        }
        System.out.println("User successfully checked");
        return user;
    }


}
