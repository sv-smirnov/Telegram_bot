package ru.geekbrains.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.geekbrains.config.BotConfig;
import ru.geekbrains.dto.TaskDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Component
public class TimeTaskBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TimeTaskBot(BotConfig config) {
        this.config = config;
        restTemplate = new RestTemplate();
    }

    private RestTemplate restTemplate;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case ("/start"):
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case ("/help"):
                    helpCommandReceived(chatId);
                    break;
                case ("/takenewtask"):
                    String answer = "Введите номер задачи в формате /takenewtask XXXXX";
                    sendMessage(chatId, answer);
                    break;
                default:
                    if (messageText.matches("^/takenewtask [A-Za-z0-9]*$")) {
                        String task = messageText.substring(13);
                        Instant instant = Instant.ofEpochSecond(update.getMessage().getDate());
                        LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(3));
                        String userName = update.getMessage().getFrom().getFirstName();
                        String lastName = update.getMessage().getFrom().getLastName();
                        String nick = update.getMessage().getFrom().getUserName();
                        Long userId = update.getMessage().getFrom().getId();
                        sendMessage(chatId, userName + " " + lastName + " " + " : " + time + " : " + task);
                        TaskDto taskDto = new TaskDto(userId, userName, lastName, task, time);
                        restTemplate.postForObject("http://localhost:8082/", taskDto, TaskDto.class);
                        break;
                    }
                    sendMessage(chatId, "Данная команда не поддерживается. Отправьте /help для вывода списка команд");
                    break;
            }

        }

    }

    private void helpCommandReceived(long chatId) {
        String answer = "Вы можете воспользоваться командой из списка для работы с ботом: \n" +
                "<b>/help </b>- выдаст список поддерживаемых команд \n" +
                "<b>/takenewtask </b> - взять в работу задачу \n";
        sendMessage(chatId, answer);
    }

    private void startCommandReceived(long chatId, String firstName) {
        String answer = firstName + ", Вас привествует Time&Task Bot, Вы можете отправить /help для вывода списка  поддерживаемых команд";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.enableHtml(true);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
