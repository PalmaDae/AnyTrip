package org.example.tgBot.handlers;

import org.example.model.Shedule;
import org.example.util.ReflectionUtil;
import org.example.util.ScannerUtils;
import org.json.JSONObject;

import org.example.service.SheduleProcessor;

import java.util.Scanner;

public class StartCommands {
    public final static Scanner scanner = new Scanner(System.in);


    public void start(){
        JSONObject lastRequest = SheduleProcessor.loadLastRequestOfShedule();

        Shedule shedule = Shedule.builder()
                .station( lastRequest.optString("station", ""))
                .transport(lastRequest.optString("transport", ""))
                .date(lastRequest.optString("date", ""))
                .build();


        if (ReflectionUtil.IsNotEmptyFields(shedule)) {
            System.out.println("Введите данные, сейвов нет");

            ReflectionUtil.setAllFields(shedule,
                    ScannerUtils.askStationCode(scanner),
                    ScannerUtils.getTransportTypeFromScanner(scanner),
                    ScannerUtils.getDate(scanner));


        } else {
            System.out.println("Использовать сохранённые данные? Y/N");
            String answear = scanner.nextLine().toLowerCase();
            if (answear.equals("N")) {
                ReflectionUtil.setAllFields(shedule,
                        ScannerUtils.askStationCode(scanner),
                        ScannerUtils.getTransportTypeFromScanner(scanner),
                        ScannerUtils.getDate(scanner));

//                ReflectionUtil.setAllFields(shedule );
//                shedule.setStation(askStationCode(scanner));
//                shedule.setTransport(getTransportTypeFromScanner(scanner));
//                shedule.setDate(getDate(scanner));
                }
            }


        }
}
