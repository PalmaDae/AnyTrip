import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static final String apiKey = "82cfd045-18ee-49da-ad53-885ebe601d94"; //Cын, он же api ключ
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
            JSONArray jsonArray = jsonObject.optJSONArray("schedule");

            if (jsonArray != null) {
                List<JSONObject> list = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(jsonArray.getJSONObject(i));
                }

                list.sort((first, second) -> { //Сортировочка, можно через компаратор, но в это надо чутооооок пострадать
                    String firstDeparture = first.optString("departure", ""); //Вот тут я ебался почти час, в итоге всё откатил, лучше сделать через библиотечку с датой
                    String secondDeparture = second.optString("departure", ""); //Но почему-то, когда я пытаюсь, у меня вылезает миллион эксепшенов

                    return firstDeparture.compareTo(secondDeparture);
                });

                int cnt = 0; //Счётчик юзлес, считай, просто так стоит, не даёт выводить миллион ответов
                for (int i = 0; i < list.size() && cnt <= 3; i++) {
                    JSONObject item = list.get(i);
                    JSONObject thread = item.optJSONObject("thread"); //Тут важно для себя вывести JSON файл, а то иногда бывает путаница, что ты парсишь вообще
                    if (thread != null) {
                        String title = thread.optString("title", "Нет данных");
                        String departure = item.optString("departure", "Нет данных");
                        String terminal = item.optString("terminal", "Нет данных");
                        String platform = item.optString("platform", "Нет данных");

                        System.out.println("Рейс: " + title);
                        System.out.println("Время отправления: " + departure);
                        System.out.println("Терминал: " + terminal);
                        System.out.println("Платформа: " + platform);
                        System.out.println();
                        cnt++;
                    } else {
                        System.out.println("Информация о рейсе отсутствует.");
                    }
                }
            }
        } else {
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

    public static String getTransportType(Scanner scanner) { //Типы транспорта, будто можно будет в будущем можно enum сделать, чтобы через кириллицу всё было
        System.out.println("Введите тип транспорта (plane, bus, train, suburban, water, helicopter):");
        String transport_type = scanner.nextLine().toLowerCase();
        List<String> listOfTransport = Arrays.asList("plane", "bus", "train", "suburban", "water", "helicopter"); //НЕ ИСПОЛЬЗОВАТЬ ARRAYLIST
        while (!listOfTransport.contains(transport_type)) {
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

        while (date.length() != 10 || !date.matches("\\d{4}-\\d{2}-\\d{2}")) { //Без регулярок выглядет понятнее, но более уродливо)
            System.out.println("Неверный формат даты");
            date = scanner.nextLine();
        }

        return date;
    }

    public static String getScheduleRequestUrl() { //Собиратель нашего url'а, вообще, тут можно ещё ммного чего сделать, но я считаю это золотой серединой
        String code = askStationCode(scanner);

        String transport_type = getTransportType(scanner);

        String date = getDate(scanner);

        return "https://api.rasp.yandex.net/v3.0/schedule/?apikey=" + apiKey + "&station=s" + code + "&transport_types=" + transport_type + "&date=" + date;
    }

    public static void main(String[] args) {
        String urlString = getScheduleRequestUrl();

        JSONObject jsonText = getJSON(urlString);

        printSchedule(jsonText);
    }
}