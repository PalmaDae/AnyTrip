package org.example.util;

import org.example.DTO.SheduleResponce;
import org.example.service.SomethingProcessor;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ScannerUtils {

    public static void printSchedule(JSONObject jsonObject) {
        if (jsonObject != null) {
            SomethingProcessor somethingProcessor = new SomethingProcessor();
            List<SheduleResponce> listShedules = somethingProcessor.parseAndGetList(jsonObject);

            for (SheduleResponce sheduleResponce : listShedules){
                System.out.println(sheduleResponce.toString());
            }
        }

        else {
            System.out.println("DataBase ERROR");
        }
    }

    public static String askStationCode(Scanner scanner) { //Получаем код станции, вообще он расчитан ещё на s, но нахуй он нужен если можно просто в url прописать)
        System.out.println("Введите код станции (7 цифр):");
        String code = scanner.nextLine();

        while (code.length() != 7) {
            System.out.println("Код должен состоять из 7 цифр");
            code = scanner.nextLine();
        }

        return code;
    }

    public static String getTransportTypeFromScanner(Scanner scanner) { //Типы транспорта, будто можно будет в будущем можно enum сделать, чтобы через кириллицу всё было
        System.out.println("Введите тип транспорта (plane, bus, train, suburban, water, helicopter):");
        String transport_type = scanner.nextLine().toUpperCase();
        List<TransportTypes> listTransportTypes = Arrays.asList(TransportTypes.values()); //НЕ ИСПОЛЬЗОВАТЬ ARRAYLIST


        while (!listTransportTypes.contains(TransportTypes.valueOf(transport_type))) {
            System.out.println("Транспорт введён неверно");
            transport_type = scanner.nextLine();
        }

        return transport_type;
    }

    public static String getDate(Scanner scanner) { //Самая ... часть, и требующая лучшей оптимизации
        System.out.println("Введите дату в формате YYYY-MM-DD:");
        String date = scanner.nextLine();

        if (date.isEmpty()) { //Работает через раз, почему? я не знаю
            date = LocalDate.now().toString();
        }

        while (date.length() != 10 || !date.matches("\\d{4}-\\d{2}-\\d{2}")) { //Без регулярок выглядит понятнее, но более уродливо)
            System.out.println("Неверный формат даты");
            date = scanner.nextLine();
        }

        return date;
    }
}
