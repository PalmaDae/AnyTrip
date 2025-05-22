package org.example.tgBot;

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
    MessageUtil messageUtil;


    public MyBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        messageUtil= new MessageUtil(telegramClient);
    }

    private void handleCallbackQuery(Update update) throws TelegramApiException {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        telegramClient.execute(AnswerCallbackQuery.builder()
            .callbackQueryId(update.getCallbackQuery().getId())
            .build());

        switch (callbackData) {
            case "CallBack":
                tryTo(newTextMessage("Yeap", chatId));
                break;
            case "Second_Callback":
                tryTo(newTextMessage("Yeah", chatId));
                break;
            case "FirstHistoryBack":
                tryTo(newTextMessage("0_0", chatId));
                break;
            case "SecondHistoryBack":
                tryTo(newTextMessage("-_-", chatId));
                break;
            default:
                newTextMessage("Unknown command", chatId);
        }
    }

    public void handleMessage(Update update) throws TelegramApiException {
        long chat_id = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        SendMessage sendMessage;

        TgMessages tgMessages = TgMessages.getCommand(messageText);

        switch (tgMessages) {
            case START:
                sendMessage = newTextMessage("Привет! \n\nЭто AnyTrip - помощник в поиске маршрута, используя ЯндексРасписание" +
                        "\n\nВот доступные команды:" +
                        "\n/keyboard - Выведет кнопки для работы с ботом" +
                        "\n/start - Начало работы с ботом, а также вывод этой информации", chat_id);
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
                sendTwoInlineKeyboard(chat_id, "First Trip", "CallBack", "Second Trip",
                        "Second_Callback", "Here you can find your favorite trips");
                break;

            case TgMessages.HISTORY_OF_TRIPS:
                sendTwoInlineKeyboard(chat_id, "First Trip", "FirstHistoryBack",
                        "Second Trip", "SecondHistoryBack", "Here you can find your history of trips");
                break;

            case TgMessages.SEARCH_OF_TRIPS: //Сюда закинуть функцию чтобы искать маршрут
                sendMessage = newTextMessageRemoveKeyboard("Here you can find trip to Kazakhstan!", chat_id, true);
                tryTo(sendMessage);
                break;

            default:
                sendMessage = newTextMessage(messageText, chat_id);
                tryTo(sendMessage);
        }
    }

    @Override
    public void consume(Update update) {
        try {
            if (update.hasMessage()) {
                handleMessage(update);
            } else if (update.hasCallbackQuery()) {
                handleCallbackQuery(update);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public static SendMessage newTextMessageRemoveKeyboard(String message_text, long chat_id, boolean flag) throws TelegramApiException{
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

    public void sendTwoInlineKeyboard(long chat_id, String firstText, String firstCallBack, String secondText, String secondCallback, String mainText) throws TelegramApiException{
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton
                        .builder()
                        .text(firstText)
                        .callbackData(firstCallBack)
                        .build()
                ))
                .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton
                        .builder()
                        .text(secondText)
                        .callbackData(secondCallback)
                        .build()
                ))
                .build();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chat_id)
                .text(mainText)
                .replyMarkup(keyboardMarkup)
                .build();
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

