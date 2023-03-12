package ru.geekbrains.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class Notifier {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BOT_URL = "http://localhost:8081/";

    public Notifier() {
    }

    @Scheduled(cron = "0 30 17 * * mon-fri")
    public void sendAddTaskReminder(){
        ResponseEntity<String> response = restTemplate.getForEntity(BOT_URL + "add_task_reminder", String.class);
        log.info("Add task notify was send to bot");

    }
}
