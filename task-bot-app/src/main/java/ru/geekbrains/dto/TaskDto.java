package ru.geekbrains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TaskDto {
    private Long userId;
    private String userName;
    private String lastName;
    private String task;
    private LocalDateTime time;

}
