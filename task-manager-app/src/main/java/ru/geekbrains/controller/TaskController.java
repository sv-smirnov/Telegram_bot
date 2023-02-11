package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.dto.TaskDto;
import ru.geekbrains.entity.Task;
import ru.geekbrains.service.TaskService;

import java.time.LocalDate;
import java.util.List;


@RestController
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("/history")
    public String historyForDate(@Param("date") String date) {
        System.out.println(date);
       return taskService.history(LocalDate.parse(date)).toString();

    }
    //TODO historyBetween

    @PostMapping("/")
    public void addNewTask(@RequestBody TaskDto taskDto){
        taskService.createNewTask(taskDto);
    }

}
