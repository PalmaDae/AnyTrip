package org.example.tgBot.handlers;

import org.example.DTO.SheduleRequest;
import org.example.tgBot.service.RequestSheduleService;
import org.example.tgBot.util.Keyboard;
import org.example.tgBot.util.TgMessages;
import org.example.util.TransportTypes;
import org.example.util.СonditionsRequests;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.regex.Pattern;

import static org.example.util.TransportTypes.listTransportTypes;

public class MessageHandler implements IHandler {
    private final TelegramClient telegramClient;
    private final Keyboard keyboard;
    private SheduleRequest sheduleRequest;

    public MessageHandler(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
        this.keyboard = new Keyboard(telegramClient);
    }

    @Override
    public boolean canHandle(Update update) {
        return false;
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
            handleCode(chatId, messageText);
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
                sendMessage = newTextMessage("Стартуем!", chatId);
                break;

            case KEYBOARD:
                sendMessage = createKeyboardMessage(chatId);
                break;

            case FAVORITE_TRIPS:
                handleFavoriteTrips(chatId);
                break;

            case HISTORY_OF_TRIPS:
                try {
                    sendMessage = keyboard.newTextMessageRemoveKeyboard(
                            "There is you can find your history of trips", chatId, true);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
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

    private SendMessage createKeyboardMessage(long chatId) {
        SendMessage message = newTextMessage("Гляди и любуйся!", chatId);
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow("Избранные маршруты"))
                .keyboardRow(new KeyboardRow("История маршрутов"))
                .keyboardRow(new KeyboardRow("Поиск маршрута"))
                .build());
        return message;
    }

    private void handleFavoriteTrips(long chatId) {
        try {
            keyboard.sendInlineKeyboard(chatId);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void startTripSearch(long chatId) {
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton
                        .builder()
                        .text("Отменить ввод")
                        .callbackData("Some_remove")
                        .build()))
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("🔍 Введите код станции для поиска поездок:")
                .replyMarkup(keyboardMarkup)
                .build();

        СonditionsRequests.WAIT_INPUT_SHEDULE = true;
        СonditionsRequests.WAIT_INPUT_CODE = true;
        this.sheduleRequest = new SheduleRequest(true);

        tryTo(message);
    }

    private void handleCode(long chatId, String codeText) {
        if (!СonditionsRequests.WAIT_INPUT_SHEDULE) return;

        if (Pattern.matches("^\\d{7}$", codeText)) {
            sheduleRequest.setStation(codeText);
            СonditionsRequests.WAIT_INPUT_CODE = false;
            СonditionsRequests.WAIT_INPUT_TRANSPORT = true;
            tryTo(newTextMessage("Введите тип транспорта:", chatId));
        } else {
            tryTo(newTextMessage("Код введен неверно. Введите 7 цифр от 1 до 9:", chatId));
        }
    }

    private void handleTransport(long chatId, String transportText) {
        if (!СonditionsRequests.WAIT_INPUT_SHEDULE) return;

        transportText = transportText.toUpperCase();
        try {
            TransportTypes transport = TransportTypes.valueOf(transportText);
            if (listTransportTypes.contains(transport)) {
                sheduleRequest.setTransport(transportText);
                СonditionsRequests.WAIT_INPUT_TRANSPORT = false;
                СonditionsRequests.WAIT_INPUT_DATE = true;
                tryTo(newTextMessage("Введите дату в формате ГГГГ-ММ-ДД:", chatId));
            } else {
                tryTo(newTextMessage("Некорректный тип транспорта. Введите повторно:", chatId));
            }
        } catch (IllegalArgumentException e) {
            tryTo(newTextMessage("Некорректный тип транспорта. Введите повторно:", chatId));
        }
    }

    private void handleDate(long chatId, String dateText) {
        if (!validateScheduleState(chatId)) return;

        if (Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dateText)) {
            sheduleRequest.setDate(dateText);
            СonditionsRequests.WAIT_INPUT_DATE = false;
            tryTo(newTextMessage("Данные сохранены. Расписание: \n" + RequestSheduleService.getSgedule(sheduleRequest), chatId));
        } else {
            tryTo(newTextMessage("Дата введена неверно. Введите в формате ГГГГ-ММ-ДД:", chatId));
        }
    }

    private boolean validateScheduleState(long chatId) {
        if (!СonditionsRequests.WAIT_INPUT_SHEDULE) {
            СonditionsRequests.resetAllFieldsOnFalse();
            tryTo(newTextMessage("Сессия поиска завершена. Начните заново.", chatId));
            return false;
        }
        return true;
    }
}