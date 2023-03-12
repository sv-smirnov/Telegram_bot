package ru.geekbrains.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String token;
    @Value("${bot.task_manager_url}")
    private String taskManagerUrl;
}
