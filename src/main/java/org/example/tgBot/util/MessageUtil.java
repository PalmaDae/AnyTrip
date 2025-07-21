package org.example.tgBot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class MessageUtil {
    private final TelegramClient telegramClient;

    public MessageUtil(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }


    public static SendMessage newTextMessage(String text, Long chatId) {
        return SendMessage.builder().chatId(chatId.toString()).text(text).build();
    }
}
