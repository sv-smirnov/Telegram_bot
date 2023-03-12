package ru.geekbrains.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public void createNewTask(TaskDto taskDto) {
        User user = createUser(taskDto);
        Task task = new Task(taskDto.getTask(), user, taskDto.getTime(), taskDto.getDate());
        taskRepository.save(task);
        log.info("New task added");
    }

    private User createUser(TaskDto taskDto) {
        User user = new User(taskDto.getUserId(), taskDto.getUserName(), taskDto.getLastName());
        if (!userRepository.existsById(taskDto.getUserId())) {
            userRepository.save(user);
            log.info("New user added");
        }
        return user;
    }

    public List<Task> history(LocalDate date, Long userId) {
        List<Task> history = taskRepository.history(date, userId);
        log.info("History is returned");
        return history;
    }


    public List<Task> teamActivityForDate(LocalDate date) {
        List<Task> teamActivity = taskRepository.teamActivityForDate(date);
        log.info("Team activity is returned");
        return teamActivity;
    }

    public List<Long> getUsersChatIds() {
        List<Long> usersChatId = userRepository.getUsersChatIds();
        return usersChatId;
    }

    public List<Task> historyBetween(LocalDate startDate, LocalDate endDate, Long userId) {
        List<Task> history = taskRepository.historyBetween(startDate, endDate, userId);
        log.info("History for period is returned");
        return history;
    }
}
