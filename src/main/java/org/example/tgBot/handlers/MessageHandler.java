package org.example.tgBot.handlers;

import org.example.DTO.SheduleRequest;
import org.example.RaspRequestBuilder;
import org.example.tgBot.util.Keyboard;
import org.example.tgBot.util.TgMessages;
import org.example.util.ClosedStrings;
import org.example.util.TransportTypes;
import org.example.util.СonditionsRequests;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.example.util.TransportTypes.listTransportTypes;

public class MessageHandler implements IHandler {
    private final TelegramClient telegramClient;
    private final Keyboard keyboard;
    private SheduleRequest sheduleRequest;

    public MessageHandler(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
        this.keyboard = new Keyboard(telegramClient);
    }

    public void tryTo(SendMessage sendMessage) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static SendMessage newTextMessage(String messageText, long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        // Обработка состояний ожидания ввода
        if (!(update.hasCallbackQuery()) & handleInputStates(chatId, messageText) ) {
            return;
        }

        // Обработка команд
        handleCommands(chatId, messageText);
    }

    private boolean handleInputStates(long chatId, String messageText) {

        if (СonditionsRequests.WAIT_INPUT_CODE) {
            handleForCity1(chatId, messageText);
            return true;
        }
        if (СonditionsRequests.WAIT_TWO_CITIES) {
            handleForCity2(chatId, messageText);
            return true;
        }

        if (СonditionsRequests.WAIT_INPUT_TRANSPORT) {
            handleTransport(chatId, messageText);
            return true;
        }
        if (СonditionsRequests.WAIT_INPUT_DATE) {
            handleDate(chatId, messageText);
            return true;
        }
        return false;
    }

    private void handleCommands(long chatId, String messageText) {
        TgMessages tgMessages = TgMessages.getCommand(messageText);
        SendMessage sendMessage = null;

        switch (tgMessages) {
            case START:
                sendMessage = startKeyboardMarkup(chatId);
                break;

            case SEARCH_OF_TRIPS:
                startTripSearch(chatId);
                return;

            case STATION_CODE:
                sendMessage = newTextMessage("код введен", chatId);
                break;

            default:
                sendMessage = newTextMessage(messageText, chatId);
        }

        if (sendMessage != null) {
            tryTo(sendMessage);
        }
    }

    private SendMessage startKeyboardMarkup(long chatId) {
        SendMessage message = newTextMessage("Вот возможности бота!", chatId);

        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow("Поиск маршрута"))
                        .keyboardRow(new KeyboardRow("Информация о боте"))
                        .keyboardRow(new KeyboardRow("Избранные маршруты"))
                .build());
        return message;
    }

    private void startTripSearch(long chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("🔍 Введите 1-й город")
                .build();

        message.setReplyMarkup(new ReplyKeyboardRemove(true));

        СonditionsRequests.WAIT_INPUT_SHEDULE = true;
        СonditionsRequests.WAIT_INPUT_CODE = true;
        this.sheduleRequest = new SheduleRequest(true);

        tryTo(message);
    }

    private void handleForCity1(long chatId, String codeText) {
        if (!СonditionsRequests.WAIT_INPUT_SHEDULE) return;
        sheduleRequest.setCity1(codeText);
        СonditionsRequests.WAIT_INPUT_CODE = false;

        СonditionsRequests.WAIT_TWO_CITIES = true;
        tryTo(newTextMessage("🔍 Введите 2-й город\n", chatId));
    }

    private void handleForCity2(long chatId, String codeText) {
        if (!СonditionsRequests.WAIT_INPUT_SHEDULE) return;

        sheduleRequest.setCity2(codeText);


        СonditionsRequests.WAIT_INPUT_TRANSPORT = true;
        СonditionsRequests.WAIT_TWO_CITIES = false;

        SendMessage message = createKeyboardMessage(chatId);

        tryTo(message);
    }

    private void handleTransport(long chatId, String transportText) {
        if (!СonditionsRequests.WAIT_INPUT_SHEDULE) return;

        TransportTypes transport = TransportTypes.fromRussian(transportText);

        if (transport != null && listTransportTypes.contains(transport)) {
            sheduleRequest.setTransport(transport.toString());
            СonditionsRequests.WAIT_INPUT_TRANSPORT = false;
            СonditionsRequests.WAIT_INPUT_DATE = true;

            SendMessage message = newTextMessage("Введите дату в формате ГГГГ-ММ-ДД:", chatId);
            message.setReplyMarkup(new ReplyKeyboardRemove(true));
            tryTo(message);
        } else {
            tryTo(newTextMessage("Некорректный тип транспорта. Введите повторно:", chatId));
        }
    }

    private void handleDate(long chatId, String dateText) {
        if (!validateScheduleState(chatId)) return;

            sheduleRequest.setDate(dateText);
            СonditionsRequests.WAIT_INPUT_DATE = false;
            tryTo(newTextMessage("Данные сохранены. Расписание: \n" + RaspRequestBuilder.getInString(RaspRequestBuilder.buildSearchRequests(sheduleRequest.getCity1(), sheduleRequest.getCity2(), sheduleRequest.getTransport(), sheduleRequest.getDate(), ClosedStrings.API_KEY)) , chatId));
    }

    private boolean validateScheduleState(long chatId) {
        if (!СonditionsRequests.WAIT_INPUT_SHEDULE) {
            СonditionsRequests.resetAllFieldsOnFalse();
            tryTo(newTextMessage("Сессия поиска завершена. Начните заново.", chatId));
            return false;
        }
        return true;
    }

    private SendMessage createKeyboardMessage(long chatId) {
        SendMessage message = newTextMessage("Выбери доступный транспорт\uD83D\uDEF5", chatId);

        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow("Автобус"))
                        .keyboardRow(new KeyboardRow("Электричка"))
                        .keyboardRow(new KeyboardRow("Поезд"))
                        .keyboardRow(new KeyboardRow("Самолёт"))
                        .keyboardRow(new KeyboardRow("Вертолёт"))
                .build());

        return message;
    }
}