package org.example.tgBot.util;

public enum TgMessages {
    START("/start"),
    KEYBOARD("/keyboard"),

    FAVORITE_TRIPS("/favorites"),
    HISTORY_OF_TRIPS("/history"),
    SEARCH_OF_TRIPS("Поиск маршрута"),
    HELP("/help"),
    ADD_FAV("/addfav"),
    REMOVE_FAV("/removefav"),



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

    public void setCode(String tgMessageDescription) {
        this.tgMessageDescription = tgMessageDescription;
    }

}
