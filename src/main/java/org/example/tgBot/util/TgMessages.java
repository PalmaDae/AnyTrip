package org.example.tgBot.util;

public enum TgMessages {
    START("/start"),
    KEYBOARD("/keyboard"),

    FAVORITE_TRIPS("Избранные маршруты"),
    HISTORY_OF_TRIPS("История маршрутов"),
    SEARCH_OF_TRIPS("Поиск маршрута"),

    DEFAULT_FOR_ECHO("default"); // что-то типо заглушки, чтоб было красиво

    private String tgMessageDescription;

    TgMessages(String tgMessageDescription){
        this.tgMessageDescription = tgMessageDescription;
    }

    public static TgMessages getCommand(String descreption){
        for (TgMessages tgMessage: values()){
            if (tgMessage.tgMessageDescription.equalsIgnoreCase(descreption)){
                return tgMessage;
            }
        }
        return DEFAULT_FOR_ECHO;
    }

}
