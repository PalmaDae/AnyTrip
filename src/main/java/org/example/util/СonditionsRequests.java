package org.example.util;

public class СonditionsRequests {
    // если нажать на кнопку отменить ввод - прекращает ввод в целом
    public static boolean WAIT_INPUT_SHEDULE = false; // с ней взаимодействует InlineKeyboard


    public static boolean WAIT_INPUT_CODE = false;
    public static boolean WAIT_INPUT_TRANSPORT = false;
    public static boolean WAIT_INPUT_DATE = false;
    public static boolean WAIT_TWO_CITIES = false;

    public static void resetAllFieldsOnFalse(){
        if (!WAIT_INPUT_SHEDULE){
            WAIT_INPUT_CODE = false;
            WAIT_INPUT_TRANSPORT = false;
            WAIT_INPUT_DATE = false;
        }
    }
}