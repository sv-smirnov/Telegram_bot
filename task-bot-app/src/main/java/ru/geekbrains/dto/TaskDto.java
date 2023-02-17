package ru.geekbrains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class TaskDto {
    private Long userId;
    private String userName;
    private String lastName;
    private String task;
    private String time;
    private LocalDate date;

}
