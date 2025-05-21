package org.example.tgBot;

import org.example.tgBot.util.MessageUtil;
import org.example.tgBot.util.TgMessages;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
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
    MessageUtil messageUtil;


    public MyBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        messageUtil= new MessageUtil(telegramClient);
    }

    private void handleCallbackQuery(Update update) throws TelegramApiException {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        switch (callbackData) {
            case "first_trip":
                newTextMessage("Вы выбрали первую поездку!", chatId);
                break;
            case "second_trip":
                newTextMessage("Вторая поездка — отличный выбор!", chatId);
                break;
            default:
                newTextMessage("Неизвестная команда", chatId);
        }
    }

    public void handleMessage(Update update) throws TelegramApiException {
        long chat_id = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        SendMessage sendMessage;

        TgMessages tgMessages = TgMessages.getCommand(messageText);

        switch (tgMessages) {
            case START:
                newTextMessage("Стартуем!", chat_id);
                break;
            case TgMessages.KEYBOARD:
                    sendMessage = newTextMessage("Гляди и любуйся!",chat_id);
                    sendMessage.setReplyMarkup(ReplyKeyboardMarkup
                            .builder()
                            .keyboardRow(new KeyboardRow("Избранные маршруты"))
                            .keyboardRow(new KeyboardRow("История маршрутов"))
                            .keyboardRow(new KeyboardRow("Поиск маршрута"))
                            .build());
                    tryTo(sendMessage);
                    break;

                case TgMessages.FAVORITE_TRIPS:
                    SendMessage message = SendMessage
                        .builder()
                        .chatId(chat_id)
                        .text(message_text)
                        .replyMarkup(InlineKeyboardMarkup
                            .builder()
                            .keyboardRow(
                                    new InlineKeyboardRow(InlineKeyboardButton
                                            .builder()
                                            .text("First trip")
                                            .callbackData("Some callback Data")
                                            .build()
                                    )
                            )
                            .keyboardRow(
                                    new InlineKeyboardRow(InlineKeyboardButton
                                            .builder()
                                            .text("Second trip")
                                            .callbackData("Yeah")
                                            .build()
                                    )
                            )
                            .keyboardRow(
                                    new InlineKeyboardRow(InlineKeyboardButton
                                            .builder()
                                            .text("Third trip")
                                            .callbackData("Another one callbackdata lol")
                                            .build()
                                    )
                            )
                            .build())
                    .build();
                    try {
                        telegramClient.execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                case TgMessages.HISTORY_OF_TRIPS:
                    sendMessage = newTextMessageRemoveKeyboard("There is you can find your history of trips", chat_id, true);
                    tryTo(sendMessage);
                    break;

                case TgMessages.SEARCH_OF_TRIPS:
                    sendMessage = newTextMessageRemoveKeyboard("There is you can find trip to Kazakhstan!", chat_id, true);
                    tryTo(sendMessage);
                    break;

                default:
                    sendMessage = newTextMessage(message_text, chat_id);
                    tryTo(sendMessage);
        }
    }

    @Override
    public void consume(Update update) {
        long chat_id = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();

            System.out.println(message_text);

            SendMessage sendMessage;

            TgMessages tgMessage = TgMessages.getCommand(message_text);

            switch (tgMessage){
                case TgMessages.START:
                    sendMessage = newTextMessage("Стартуем",chat_id);
                    tryTo(sendMessage);
                    break;

                case TgMessages.KEYBOARD:
                    sendMessage = newTextMessage("Гляди и любуйся!",chat_id);
                    sendMessage.setReplyMarkup(ReplyKeyboardMarkup
                            .builder()
                            .keyboardRow(new KeyboardRow("Избранные маршруты"))
                            .keyboardRow(new KeyboardRow("История маршрутов"))
                            .keyboardRow(new KeyboardRow("Поиск маршрута"))
                            .build());
                    tryTo(sendMessage);
                    break;

                case TgMessages.FAVORITE_TRIPS:
                    SendMessage message = SendMessage
                        .builder()
                        .chatId(chat_id)
                        .text(message_text)
                        .replyMarkup(InlineKeyboardMarkup
                            .builder()
                            .keyboardRow(
                                    new InlineKeyboardRow(InlineKeyboardButton
                                            .builder()
                                            .text("First trip")
                                            .callbackData("Some callback Data")
                                            .build()
                                    )
                            )
                            .keyboardRow(
                                    new InlineKeyboardRow(InlineKeyboardButton
                                            .builder()
                                            .text("Second trip")
                                            .callbackData("Yeah")
                                            .build()
                                    )
                            )
                            .keyboardRow(
                                    new InlineKeyboardRow(InlineKeyboardButton
                                            .builder()
                                            .text("Third trip")
                                            .callbackData("Another one callbackdata lol")
                                            .build()
                                    )
                            )
                            .build())
                    .build();
                    try {
                        telegramClient.execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                case TgMessages.HISTORY_OF_TRIPS:
                    sendMessage = newTextMessageRemoveKeyboard("There is you can find your history of trips", chat_id, true);
                    tryTo(sendMessage);
                    break;

                case TgMessages.SEARCH_OF_TRIPS:
                    sendMessage = newTextMessageRemoveKeyboard("There is you can find trip to Kazakhstan!", chat_id, true);
                    tryTo(sendMessage);
                    break;

                default:
                    sendMessage = newTextMessage(message_text, chat_id);
                    tryTo(sendMessage);
            }
        }
        else if (update.hasMessage() && update.getMessage().hasPhoto()) {
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
        else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();

            if (call_data.equals("Some callback Data")) {
                SendMessage message = newTextMessage("Yeah", chat_id);
                try {
                    telegramClient.execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
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

    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals("Some callback Data")) {
                newTextMessage("There is your First Trip", chatId);
            }
        }
}
}

