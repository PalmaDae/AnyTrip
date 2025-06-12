package org.example.handler;

import org.example.api.YandexAPI;
import org.example.DTO.SheduleRequest;
import org.example.util.ReflectionUtil;
import org.example.util.ScannerUtils;
import org.json.JSONObject;

import org.example.service.SheduleProcessor;

import java.io.IOException;
import java.util.Scanner;

public class StartCommands {

    private static Scanner scanner = new Scanner(System.in);

    public static Scanner getScanner() {
        return scanner;
    }

    public static void start(){
        JSONObject lastRequest = SheduleProcessor.loadLastRequestOfShedule();

        SheduleRequest sheduleRequest = SheduleRequest.builder()
                .station( lastRequest.optString("station", ""))
                .transport(lastRequest.optString("transport", ""))
                .date(lastRequest.optString("date", ""))
                .build();


        if (ReflectionUtil.IsNotEmptyFields(sheduleRequest)) {
            System.out.println("Введите данные, сейвов нет");

            ReflectionUtil.setAllFields(sheduleRequest,
                    ScannerUtils.askStationCode(scanner),
                    ScannerUtils.getTransportTypeFromScanner(scanner),
                    ScannerUtils.getDate(scanner));


        } else {
            System.out.println("Использовать сохранённые данные? Y/N");
            String answear = scanner.nextLine().toLowerCase();
            if (answear.equals("N")) {
                ReflectionUtil.setAllFields(sheduleRequest,
                        ScannerUtils.askStationCode(scanner),
                        ScannerUtils.getTransportTypeFromScanner(scanner),
                        ScannerUtils.getDate(scanner));

                }
            }

        try {
            SheduleProcessor.saveLastRequestOfShedule(sheduleRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String urlString = YandexAPI.getScheduleRequestUrl(sheduleRequest.getStation(), sheduleRequest.getTransport(), sheduleRequest.getDate());
        JSONObject jsonText = YandexAPI.getJSON(urlString);
        ScannerUtils.printSchedule(jsonText);

        }
}
