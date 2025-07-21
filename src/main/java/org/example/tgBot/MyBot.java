package org.example.tgBot;

import org.example.tgBot.handlers.CallbackHandler;
import org.example.tgBot.handlers.MessageHandler;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class MyBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private MessageHandler messageHandler;
    private CallbackHandler callbackHandler;

    public MyBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        messageHandler= new MessageHandler(telegramClient);
        callbackHandler = new CallbackHandler(telegramClient);
    }

    @Override
    public void consume(Update update) {
        try {
            if (update.hasMessage()) {
                messageHandler.handle(update);
            } else if (update.hasCallbackQuery()) {
                callbackHandler.handle(update);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

