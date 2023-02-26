package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.service.TimeTaskBot;

import java.time.LocalDate;
@RestController
public class BotController {
    @Autowired
    TimeTaskBot timeTaskBotService;

    @GetMapping("/add_task_reminder")
    public void addTaskReminder() {
        timeTaskBotService.sendAddTaskReminder();
    }
}
