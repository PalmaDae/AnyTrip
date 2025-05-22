package org.example.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import org.example.model.Shedule;
import org.example.service.SomethingProcessor;
import org.example.util.ReflectionUtil;
import org.example.util.ClosedStrings;
import org.example.util.TransportTypes;
import org.json.JSONObject;
import org.example.model.Something;

public class YandexAPI {

    public static JSONObject getJSON(String urlByString) { //Получаем json файл, сквозь боль и слёзы
        try {
            URL url = new URL(urlByString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setConnectTimeout(50000); //Это 50 секунд, можно по идее поставить 10, на 5 он точно ругается
            connection.setReadTimeout(50000);

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) { //Если выкидывает 200, то всё кайф
                throw new Exception("HTTP error code: " + responseCode); //Самая любимая ошибка - 404)))
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new JSONObject(response.toString());
        } catch (SocketException e) {
            System.out.println("TimeOut");
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }

        return null;
    }

//...


    public static String getScheduleRequestUrl(String station, String transport, String date) {
       return "https://api.rasp.yandex.net/v3.0/schedule/?apikey=" + ClosedStrings.API_KEY + "&station=s" + station + "&transport_types=" + transport + "&date=" + date;
    }

    // set
//    public static void saveLastRequestOfShedule(String station, String transport, String date) throws IOException {
//        File myFile = new File("last_request.txt");
//
//        if (!myFile.exists()) {
//            myFile.createNewFile();
//        }
//
//        FileWriter fileWriter = new FileWriter(myFile,false);
//
//        String lineSeparator = System.lineSeparator();
//        fileWriter.write(station + lineSeparator);
//        fileWriter.write(transport + lineSeparator);
//        fileWriter.write(date + lineSeparator);
//
//        fileWriter.close();
//    }

    // get
//    public static JSONObject loadLastRequestOfShedule() throws IOException {
//        File myFile = new File("last_request.txt");
//
//        if (!myFile.exists()) {
//            return new JSONObject();
//        }
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(myFile))) {
//            StringBuilder content = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line).append(System.lineSeparator());
//            }
//
//            String[] data = content.toString().split(System.lineSeparator());
//
//            JSONObject jsonObject = new JSONObject();
//            if (data.length == 3) {
//                jsonObject.put("station", data[0]);
//                jsonObject.put("transport", data[1]);
//                jsonObject.put("date", data[2]);
//            }
//
//            return jsonObject;
//        }
//    }

//    public static void main(String[] args) throws IOException {
//        JSONObject lastRequest = loadLastRequestOfShedule();
//
//        Shedule shedule = Shedule.builder()
//                .station( lastRequest.optString("station", ""))
//                .transport(lastRequest.optString("transport", ""))
//                .date(lastRequest.optString("date", ""))
//                .build();
//
//
//        if (ReflectionUtil.IsNotEmptyFields(shedule)) {
//            System.out.println("Введите данные, сейвов нет");
//
//            ReflectionUtil.setAllFields(shedule,
//                    askStationCode(scanner),
//                    getTransportTypeFromScanner(scanner),
//                    getDate(scanner));
//
//
//        } else {
//            System.out.println("Использовать сохранённые данные? Y/N");
//            String answear = scanner.nextLine().toLowerCase();
//            if (answear.equals("N")) {
//                ReflectionUtil.setAllFields(shedule,
//                        askStationCode(scanner),
//                        getTransportTypeFromScanner(scanner),
//                        getDate(scanner));
//
////                ReflectionUtil.setAllFields(shedule );
////                shedule.setStation(askStationCode(scanner));
////                shedule.setTransport(getTransportTypeFromScanner(scanner));
////                shedule.setDate(getDate(scanner));
//            }
//        }
//
//        saveLastRequestOfShedule(shedule);
//
//        String urlString = getScheduleRequestUrl(station, transport_type, date);
//        JSONObject jsonText = getJSON(urlString);
//        printSchedule(jsonText);
//    }
}