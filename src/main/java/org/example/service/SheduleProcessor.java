package org.example.service;

import org.example.DTO.SheduleRequest;
import org.example.util.ReflectionUtil;
import org.json.JSONObject;

import java.io.*;

public class SheduleProcessor {


    // set
    public static void saveLastRequestOfShedule(SheduleRequest sheduleRequest) throws IOException {
        File myFile = new File("last_request.txt");

        if (!myFile.exists()) {
            myFile.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(myFile,false);

        String lineSeparator = System.lineSeparator();
        fileWriter.write(sheduleRequest.getStation() + lineSeparator);
        fileWriter.write(sheduleRequest.getTransport() + lineSeparator);
        fileWriter.write(sheduleRequest.getDate()+ lineSeparator);

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
            SheduleRequest sheduleRequest = new SheduleRequest(content.toString().split(System.lineSeparator()) );


            JSONObject jsonObject = new JSONObject();
            if (ReflectionUtil.IsNotEmptyFields(sheduleRequest)) {
                jsonObject.put("station", sheduleRequest.getStation());
                jsonObject.put("transport", sheduleRequest.getTransport());
                jsonObject.put("date", sheduleRequest.getDate());
            }

            return jsonObject;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
