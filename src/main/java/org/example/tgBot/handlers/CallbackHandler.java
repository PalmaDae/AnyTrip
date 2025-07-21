package org.example.tgBot.handlers;

import org.example.tgBot.util.MessageUtil;
import org.example.util.СonditionsRequests;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class CallbackHandler extends MessageHandler{

    private TelegramClient telegramClient;
    private MessageUtil messageUtil;

    public CallbackHandler(TelegramClient telegramClient){
        super(telegramClient);
        messageUtil = new MessageUtil(telegramClient);

        this.telegramClient = telegramClient;
    }

    @Override
    public void handle(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        try {
            telegramClient.execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(update.getCallbackQuery().getId())
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        switch (callbackData) {
            case "Some_callbackData":
                tryTo(messageUtil.newTextMessage("Yeah", chatId));
                break;
            case "Second_callbackData":
                tryTo(messageUtil.newTextMessage("yeap", chatId));
                break;

            case "Some_remove":
                tryTo(messageUtil.newTextMessage("❌ Ввод кода отменен", chatId));
                СonditionsRequests.WAIT_INPUT_SHEDULE = false;
                СonditionsRequests.resetAllFieldsOnFalse();
                break;

            default:
                messageUtil.newTextMessage("Unknow command", chatId);
        }
    }


    private void doRequest(String callbackData) {
    }
}
