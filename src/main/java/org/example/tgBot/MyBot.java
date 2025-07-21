package org.example.tgBot;

import okhttp3.Cache;
import org.example.tgBot.handlers.CallbackHandler;
import org.example.tgBot.handlers.MessageHandler;
import org.example.tgBot.util.MessageUtil;
import org.example.tgBot.util.TgMessages;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Comparator;
import java.util.List;

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

