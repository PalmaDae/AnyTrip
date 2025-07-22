package org.example.tgBot.util;

public enum TgMessages {
    START("/start"),

    SEARCH_OF_TRIPS("Поиск маршрута"),
    HELP("/help"),

    STATION_CODE("00"),

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