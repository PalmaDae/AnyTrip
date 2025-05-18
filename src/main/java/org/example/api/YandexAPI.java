package org.example.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import org.example.service.ScheduleProcessor;
import org.example.util.ClosedStrings;
import org.example.util.TransportTypes;
import org.json.JSONObject;
import org.example.model.Schedule;

// ЗАЕБАЛСЯ (((

public class YandexAPI {
    public final static Scanner scanner = new Scanner(System.in);

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

    public static void printSchedule(JSONObject jsonObject) {
        if (jsonObject != null) {
            ScheduleProcessor scheduleProcessor = new ScheduleProcessor();
            List<Schedule> listShedules = scheduleProcessor.parseAndGetList(jsonObject);

            for (Schedule schedule: listShedules){
                System.out.println(schedule.toString());
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

    public static String getDate(Scanner scanner) { //Самая заёбистая часть, и требующая лучшей оптимизации
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

    public static String getScheduleRequestUrl(String station, String transport, String date) {
       return "https://api.rasp.yandex.net/v3.0/schedule/?apikey=" + ClosedStrings.API_KEY + "&station=s" + station + "&transport_types=" + transport + "&date=" + date;
    }


    public static void saveLastRequest(String station, String transport, String date) throws IOException {
        File myFile = new File("last_request.txt");

        if (!myFile.exists()) {
            myFile.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(myFile,false);

        String lineSeparator = System.lineSeparator();
        fileWriter.write(station + lineSeparator);
        fileWriter.write(transport + lineSeparator);
        fileWriter.write(date + lineSeparator);

        fileWriter.close();
    }

    public static JSONObject loadLastRequest() throws IOException {
        File myFile = new File("last_request.txt");

        if (!myFile.exists()) {
            return new JSONObject();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(myFile))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }

            String[] data = content.toString().split(System.lineSeparator());

            JSONObject jsonObject = new JSONObject();
            if (data.length == 3) {
                jsonObject.put("station", data[0]);
                jsonObject.put("transport", data[1]);
                jsonObject.put("date", data[2]);
            }

            return jsonObject;
        }
    }

    public static void main(String[] args) throws IOException {
        JSONObject lastRequest = loadLastRequest();

        String station = lastRequest.optString("station", "");
        String transport_type = lastRequest.optString("transport", "");
        String date = lastRequest.optString("date", "");

        if (station.isEmpty() || transport_type.isEmpty() || date.isEmpty()) {
            System.out.println("Введите данные, сейвов нет");
            station = askStationCode(scanner);
            transport_type = getTransportTypeFromScanner(scanner);
            date = getDate(scanner);
        } else {
            System.out.println("Использовать сохранённые данные? Y/N");
            String answear = scanner.nextLine().toLowerCase();
            if (answear.equals("N")) {
                station = askStationCode(scanner);
                transport_type = getTransportTypeFromScanner(scanner);
                date = getDate(scanner);
            }
        }

        saveLastRequest(station, transport_type, date);

        String urlString = getScheduleRequestUrl(station, transport_type, date);
        JSONObject jsonText = getJSON(urlString);
        printSchedule(jsonText);
    }
}