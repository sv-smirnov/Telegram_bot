package ru.geekbrains.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.geekbrains.config.BotConfig;

@Component
public class TimeTaskBot extends TelegramLongPollingBot {
    final BotConfig config;
    public TimeTaskBot(BotConfig config) {
        this.config = config;
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
            long chatId = update.getMessage().getChatId();


            switch (messageText) {
                case("/start"):
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case("/help"):
                    helpCommandReceived(chatId);
                    break;
                case("/takenewtask"):
                    //TODO
                    break;
                default:
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

    private void sendMessage(long chatId, String textToSend){
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
