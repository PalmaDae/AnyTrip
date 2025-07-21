package org.example.tgBot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Keyboard {

    private TelegramClient telegramClient;;

    public Keyboard(TelegramClient telegramClient){
        this.telegramClient = telegramClient;
    }

    public void sendInlineKeyboard(long chat_id) throws TelegramApiException {
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton
                        .builder()
                        .text("First trip")
                        .callbackData("Some_callbackData")
                        .build()
                ))
                .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton
                        .builder()
                        .text("Second trip")
                        .callbackData("Second_callbackData")
                        .build()
                ))
                .build();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chat_id)
                .text("Choise your trip")
                .replyMarkup(keyboardMarkup)
                .build();
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static SendMessage newTextMessageRemoveKeyboard(String message_text, long chat_id, boolean flag) throws TelegramApiException{
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chat_id)
                .text(message_text)
                .replyMarkup(new ReplyKeyboardRemove(flag))
                .build();
        return sendMessage;
    }

}
