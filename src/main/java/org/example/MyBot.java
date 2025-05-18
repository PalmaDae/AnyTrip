package org.example;

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

import java.util.Comparator;
import java.util.List;

public class MyBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;


    public MyBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        long chat_id = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();

            System.out.println(message_text);

            if (message_text.equals("/start")) {
                SendMessage sendMessage = newTextMessage("Стартуем",chat_id);
                tryTo(sendMessage);
            } else if (message_text.equals("/keyboard")) {
                SendMessage sendMessage = newTextMessage("Гляди и любуйся!",chat_id);
                sendMessage.setReplyMarkup(ReplyKeyboardMarkup
                        .builder()
                        .keyboardRow(new KeyboardRow("Первая кнопка"))
                        .keyboardRow(new KeyboardRow("Вторая кнопка"))
                        .build());
                tryTo(sendMessage);
            } else if (message_text.equals("Первая кнопка")) {
                SendMessage sendMessage = newTextMessageRemoveKeyboard("Psst, i see dead people", chat_id, true);
                tryTo(sendMessage);
            } else if (message_text.equals("Вторая кнопка")) {
                SendMessage sendMessage = newTextMessageRemoveKeyboard("Это сообщение написано 11 апреля 2025 года в 17:52", chat_id, true);
                tryTo(sendMessage);
            } else {
                SendMessage sendMessage = newTextMessage(message_text, chat_id);
                tryTo(sendMessage);
            }
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            List<PhotoSize> photos = update.getMessage().getPhoto();

            String file_id = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
            .map(PhotoSize::getFileId)
            .orElse("");

            SendPhoto message = newPhotoMessage(file_id, chat_id);
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public static SendMessage newTextMessage(String message_text, long chat_id) {
        SendMessage sendMessage = SendMessage
                    .builder()
                    .chatId(chat_id)
                    .text(message_text)
                    .build();

        return sendMessage;
    }

    public static SendMessage newTextMessageRemoveKeyboard(String message_text, long chat_id, boolean flag) {
        SendMessage sendMessage = SendMessage
                    .builder()
                    .chatId(chat_id)
                    .text(message_text)
                    .replyMarkup(new ReplyKeyboardRemove(flag))
                    .build();
        return sendMessage;
    }

    public static SendPhoto newPhotoMessage(String file_id, long chat_id) {
        SendPhoto sendMessage = SendPhoto
                .builder()
                .chatId(chat_id)
                .photo(new InputFile(file_id))
                .build();

        return sendMessage;
    }

    public void tryTo(SendMessage sendMessage) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

