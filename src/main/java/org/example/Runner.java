package org.example;

import org.example.DTO.AllStationsResponse;
import org.example.api.YandexAPI;
import org.example.service.AllStationsResponseProcessor;
import org.example.tgBot.MyBot;
import org.example.util.ClosedStrings;
import org.json.JSONObject;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Runner {
    public static void runBot(){
        String botToken = ClosedStrings.TOKEN;

        AllStationsResponse response = YandexAPI.getAllStationsResponse();
        AllStationsResponseProcessor.extractRussiaStations(response);


        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new MyBot(botToken));
            System.out.println("Psst, i see dead people");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        runBot();
    }
}
