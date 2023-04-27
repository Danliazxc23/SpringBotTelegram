package io.project.SpringBotTelegram.service;

import io.project.SpringBotTelegram.config.BotConfig;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    public TelegramBot(BotConfig config) {
        this.config = config;
    }
    //обработка всех сообщений от пользователя(самый основной метод)
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText=update.getMessage().getText();
            long chatId=update.getMessage().getChatId();
            switch (messageText){
                case "/start":
                    startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
                    break;
                default:
                    sendMessage(chatId,"Sorry, command was not recognized");
            }
        }
    }
    //метод соотвествующий ответу на команду /start
    private void startCommandReceived(long chatId, String firstName) throws TelegramApiException{
        String answer="Hi "+firstName+",nice to meet you!";
        sendMessage(chatId,answer);
    }
    //метод для отправки сообщений
    private void sendMessage(long chatId,String textToSend) throws TelegramApiException{
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        }catch (TelegramApiException e){

        }

    }
    @Override
    public String getBotToken(){
        return config.getToken();
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
