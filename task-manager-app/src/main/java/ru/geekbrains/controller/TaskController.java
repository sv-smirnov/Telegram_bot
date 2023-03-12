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
    public String historyForDate(@Param("date") String date,
                                 @Param("userId") Long userId) {
       return taskService.history(LocalDate.parse(date), userId).toString();

    }
    @GetMapping("/history_btw")
    public String historyForPeriod(@Param("startDate") String startDate,
                                   @Param("endDate") String endDate,
                                 @Param("userId") Long userId) {
        return taskService.historyBetween(LocalDate.parse(startDate), LocalDate.parse(endDate), userId).toString();

    }
    @GetMapping("/team_activity")
    public String teamActivityForDate(@Param("date") String date) {
        System.out.println(date);
        return taskService.teamActivityForDate(LocalDate.parse(date)).toString();

    }

    @GetMapping("/users_list")
    public List usersList() {

        return taskService.getUsersChatIds();

    }


    @PostMapping("/")
    public void addNewTask(@RequestBody TaskDto taskDto){
        taskService.createNewTask(taskDto);
    }

}
