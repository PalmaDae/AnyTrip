# AnyTrip

**AnyTrip** - это телеграм бот, предназначенный для поиска маршрутов из одного пункта в другой.
Поддерживет разные виды транспортов

---

## Функциональность


- **/start** - начало работы с ботом
- **Поиск маршрута** - ввод данных для поиска маршрута
- **/help** - краткий экскурс в работу с ботиком
---

## Технологический стек

- **Java 21**
- **Telegram Bots Meta**
- **lombok** - создание конструкторов т.д.
- **jackson** - обработка json файлов

---

## Установка и запуск

1. Клонируйте репозиторий

    ```bash
    git clone https://github.com/PalmaDae/AnyTrip
   
2. Создайте в пакете util ClosedStrings класс, там вы укажите ваш API-KEY от API Яндекс Расписаний и токен от телеграм бота
```bash
//Пример готового ClosedStrings
public class ClosedStrings {
   public static final String API_KEY = "ЗДЕСЬ КЛЮЧ";
   public static final String TOKEN = "ЗДЕСЬ ТОКЕН";
}
```
3. Получить API_KEY вы можете в кабинете разработчика яндекс
   https://developer.tech.yandex.ru/

4. Получить TOKEN вы можете с помощью бота @BotFather в телеграм

---

## Структура проекта
    src
    ├── main
    │   └── java
    │       └── org
    │           └── example
    │               ├── api
    │               │   └── YandexAPI.java //Класс для обращения к API
    │               ├── DTO
    │               │   ├── AllStationsResponse.java //Класс дл обработки json'ов
    │               │   ├── RouteInfo.java
    │               │   └── SheduleRequest.java
    │               ├── RaspRequestBuilder.java
    │               ├── Runner.java //Класс запускающий нашего бота
    │               ├── service
    │               │   └── AllStationsResponseProcessor.java
    │               ├── tgBot
    │               │   ├── handlers
    │               │   │   ├── CallbackHandler.java //Обработчик callback'ов
    │               │   │   ├── IHandler.java
    │               │   │   └── MessageHandler.java
    │               │   ├── MyBot.java
    │               │   └── util
    │               │       ├── Keyboard.java
    │               │       ├── MessageUtil.java
    │               │       └── TgMessages.java //enum доступных сообщений в боте
    │               └── util
    │                   ├── TransportTypes.java
    │                   └── СonditionsRequests.java
    └── META-INF
        └── MANIFEST.MF

---

## Авторы:
- PalmaDae
- [User-cyber-new](https://github.com/User-cyber-new?tab=repositories)