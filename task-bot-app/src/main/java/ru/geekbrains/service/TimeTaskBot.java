package ru.geekbrains.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.geekbrains.config.BotConfig;
import ru.geekbrains.dto.TaskDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Component
@RequiredArgsConstructor
public class TimeTaskBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

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
                case ("/task"):
                    String answer = "Укажите выполненную работу в формате: /task ЧЧ:MM ЗАДАНИЕ";
                    sendMessage(chatId, answer);
                    break;
                case ("/date"):
                    String answer2 = "Укажите выполненную работу в формате: /date ГГГГ-ММ-ДД ЧЧ:MM ЗАДАНИЕ";
                    sendMessage(chatId, answer2);
                    break;
                default:
                    if ((messageText.startsWith("/task ")) && (messageText.substring(6, 11).matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
                        String task = messageText.substring(12);
                        String time = messageText.substring(6, 11);
                        LocalDate date = getMessageDateTime(update.getMessage());
                        String userName = update.getMessage().getFrom().getFirstName();
                        String lastName = update.getMessage().getFrom().getLastName();
                        String nick = update.getMessage().getFrom().getUserName();
                        Long userId = update.getMessage().getFrom().getId();
                        sendMessage(chatId, userName + " " + lastName + " " + " : " + date + " : " + task + " : " + time);
                        TaskDto taskDto = new TaskDto(userId, userName, lastName, task, time, date);
                        restTemplate.postForObject("http://localhost:8082/", taskDto, TaskDto.class);
                        break;
                    }
                    if ((messageText.startsWith("/date ")) && (messageText.substring(6, 16).matches("^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$")) && (messageText.substring(17, 22).matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
                        String task = messageText.substring(23);
                        String time = messageText.substring(17, 22);
                        LocalDate date = LocalDate.parse(messageText.substring(6, 16));
                        String userName = update.getMessage().getFrom().getFirstName();
                        String lastName = update.getMessage().getFrom().getLastName();
                        String nick = update.getMessage().getFrom().getUserName();
                        Long userId = update.getMessage().getFrom().getId();
                        sendMessage(chatId, userName + " " + lastName + " " + " : " + date + " : " + task + " : " + time);
                        TaskDto taskDto = new TaskDto(userId, userName, lastName, task, time, date);
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
                "<b>/task </b> - отчет за сегоднешний день \n" +
                "<b>/date </b> - отчет за прошедший день \n";
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

    private LocalDate getMessageDateTime(Message message) {
        Instant instant = Instant.ofEpochSecond(message.getDate());
        LocalDate time = LocalDate.ofInstant(instant, ZoneOffset.UTC);
        return time;
    }

}
