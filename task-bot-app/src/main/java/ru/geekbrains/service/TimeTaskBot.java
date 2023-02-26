package ru.geekbrains.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.geekbrains.config.BotConfig;
import ru.geekbrains.dto.TaskDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


@Component
//@RequiredArgsConstructor
public class TimeTaskBot extends TelegramLongPollingBot {
    private static final String TASK_MANAGER_URL = "http://localhost:8082/";
    //TODO logging
    private final BotConfig config;
    private final RestTemplate restTemplate = new RestTemplate();


    public TimeTaskBot(BotConfig config) {
        this.config = config;
        List<BotCommand> menuCommands = new ArrayList<>();
        menuCommands.add(new BotCommand("/start", "старт бота"));
        menuCommands.add(new BotCommand("/help", "список комманд"));
        menuCommands.add(new BotCommand("/team_activity", "сделано командой сегодня"));
        try {
            this.execute(new SetMyCommands(menuCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

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

            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();
            String lastName = update.getMessage().getFrom().getLastName();
            String nick = update.getMessage().getFrom().getUserName();
            Long userId = update.getMessage().getFrom().getId();

            if (messageText.equals("/start")){
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else if (messageText.equals("/help")){
                helpCommandReceived(chatId);
            } else if ((messageText.startsWith("/task ")) && (messageText.substring(6, 11).matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
                String task = messageText.substring(12);
                String time = messageText.substring(6, 11);
                LocalDate date = getMessageDateTime(update.getMessage());
                sendMessage(chatId, userName + " " + lastName + " " + " : " + date + " : " + task + " : " + time);
                TaskDto taskDto = new TaskDto(userId, userName, lastName, task, time, date);
                restTemplate.postForObject(TASK_MANAGER_URL, taskDto, TaskDto.class);

            } else if ((messageText.startsWith("/date ")) && (messageText.substring(6, 16).matches("^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$")) && (messageText.substring(17, 22).matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
                String task = messageText.substring(23);
                String time = messageText.substring(17, 22);
                LocalDate date = LocalDate.parse(messageText.substring(6, 16));
                sendMessage(chatId, userName + " " + lastName + " " + " : " + date + " : " + task + " : " + time);
                TaskDto taskDto = new TaskDto(userId, userName, lastName, task, time, date);
                restTemplate.postForObject(TASK_MANAGER_URL, taskDto, TaskDto.class);
            } else if ((messageText.startsWith("/history ")) && (messageText.substring(9, 19).matches("^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$"))) {
                LocalDate date = LocalDate.parse(messageText.substring(9, 19));
                sendMessage(chatId, userName + " " + lastName + " " + " запросил(а) историю работы от " + date);
                ResponseEntity<String> response = restTemplate.getForEntity(TASK_MANAGER_URL + "history?date={date}&userId={userId}", String.class, date, userId);
                String history = response.getBody();
                sendMessage(chatId, "История за " + date + ": " + history);
                //TODO /history_between
            }   else if (messageText.equals("/team_activity")){
                LocalDate date = getMessageDateTime(update.getMessage());
                getTeamActivity(chatId, date);

            } else if ((messageText.startsWith("/team_activity ")) && (messageText.substring(15, 25).matches("^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$"))) {
                LocalDate date = LocalDate.parse(messageText.substring(15, 25));
                getTeamActivity(chatId, date);
            } else {
                sendMessage(chatId, "Данная команда не поддерживается либо была не корректно введена. Отправьте /help для вывода списка команд");
            }

        }

    }



    private void helpCommandReceived(long chatId) {
        String answer = "Вы можете воспользоваться командой из списка для работы с ботом: \n" +
                "<b>/help </b>- выдаст список поддерживаемых команд; \n" +
                "<b>/task HH:MM TASK_DESCRIPTION</b> - Укажите выполненную работу в формате: /task ЧЧ:MM ЗАДАНИЕ \n" +
                "<b>/date YYYY:MM:DD HH:MM TASK_DESCRIPTION</b> - Укажите выполненную работу в формате: /date ГГГГ-ММ-ДД ЧЧ:MM ЗАДАНИЕ \n" +
                "<b>/history YYYY:MM:DD</b> - Просмотреть свою историю заданий за определенную дату в формате: /history ГГГГ-ММ-ДД \n" +
                "<b>/history_btw YYYY:MM:DD YYYY:MM:DD</b> - Просмотреть свою историю заданий за диапазон дат в формате: /history_btw ГГГГ-ММ-ДД ГГГГ-ММ-ДД\n" +
                "<b>/team_activity</b> - Просмотреть историю заданий команды за текущий день \n" +
                "<b>/team_activity YYYY:MM:DD</b> - Просмотреть историю заданий команды за определенную дату в формате: /team_activity ГГГГ-ММ-ДД] \n" ;
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
    private void getTeamActivity(long chatId, LocalDate date) {
        ResponseEntity<String> response = restTemplate.getForEntity(TASK_MANAGER_URL + "team_activity?date={date}", String.class, date);
        String teamActivity = response.getBody();
        sendMessage(chatId, "Выполненные командой задачи за " + date + ": " + teamActivity);
    }

    public void sendAddTaskReminder(){
        ResponseEntity<List> response = restTemplate.getForEntity(TASK_MANAGER_URL + "users_list", List.class);
        List users = response.getBody();
        System.out.println(users);
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i));
            sendMessage((Integer) users.get(i), "Не забудьте внести данные о выполненных задачах за сегодня!");
        }

    }

}
