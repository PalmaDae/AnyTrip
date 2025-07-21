package org.example.tgBot.util;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public class MessageUtil {
    private final TelegramClient telegramClient;

    public MessageUtil(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }


    public void tryTo(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SendMessage newTextMessage(String text, Long chatId) {
        return SendMessage.builder().chatId(chatId.toString()).text(text).build();
    }

    public static SendMessage newTextMessageRemoveKeyboard(String text, Long chatId, boolean remove) {
        SendMessage message = newTextMessage(text, chatId);
        // добавь удаление клавиатуры здесь при необходимости
        return message;
    }
}
