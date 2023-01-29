package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.dto.TaskDto;
import ru.geekbrains.service.TaskService;


@RestController
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping("/")
    public void addNewTask(@RequestBody TaskDto taskDto){
        taskService.createNewTask(taskDto);
    }

}
