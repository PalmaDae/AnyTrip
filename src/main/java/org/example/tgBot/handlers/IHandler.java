package org.example.tgBot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface IHandler {
    void handle(Update update);
}
