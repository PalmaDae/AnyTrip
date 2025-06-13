    package org.example.api;

    import java.io.*;
    import java.net.HttpURLConnection;
    import java.net.SocketException;
    import java.net.URL;

    import org.example.util.ClosedStrings;
    import org.json.JSONObject;

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


        public static String getScheduleRequestUrl(String station, String transport, String date) {
           return "https://api.rasp.yandex.net/v3.0/schedule/?apikey=" + ClosedStrings.API_KEY + "&station=s" + station + "&transport_types=" + transport + "&date=" + date;
        }

        public static String getAllStationsRequestUrl() {
            return "https://api.rasp.yandex.net/v3.0/stations_list/?apikey=" + ClosedStrings.API_KEY + "&lang=ru_RU&format=json";
        }
    }