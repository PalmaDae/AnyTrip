package org.example.service;

import org.example.api.YandexAPI;
import org.example.model.Shedule;
import org.example.util.ReflectionUtil;
import org.json.JSONObject;

import java.io.*;

public class SheduleProcessor {


    // set
    public static void saveLastRequestOfShedule(Shedule shedule) throws IOException {
        File myFile = new File("last_request.txt");

        if (!myFile.exists()) {
            myFile.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(myFile,false);

        String lineSeparator = System.lineSeparator();
        fileWriter.write(shedule.getStation() + lineSeparator);
        fileWriter.write(shedule.getTransport() + lineSeparator);
        fileWriter.write(shedule.getDate()+ lineSeparator);

        fileWriter.close();
    }

    // get
    public static JSONObject loadLastRequestOfShedule() {
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
            //создаем расписание и через рефлексию заполняем поля в Shedule ... P.S удобная вещь - если будем добавлять
            // новые поля, то не понадобится каждое из них заполнять вручную.
            Shedule shedule = new Shedule(content.toString().split(System.lineSeparator()) );


            JSONObject jsonObject = new JSONObject();
            if (ReflectionUtil.IsNotEmptyFields(shedule)) {
                jsonObject.put("station",shedule.getStation());
                jsonObject.put("transport", shedule.getTransport());
                jsonObject.put("date", shedule.getDate());
            }

            return jsonObject;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
