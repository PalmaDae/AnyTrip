package org.example.tgBot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface IHandler {

    boolean canHandle(Update update);
    void handle(Update update);

}
