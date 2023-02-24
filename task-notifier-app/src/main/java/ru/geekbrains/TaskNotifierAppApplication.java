package ru.geekbrains;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskNotifierAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskNotifierAppApplication.class, args);
    }

}
