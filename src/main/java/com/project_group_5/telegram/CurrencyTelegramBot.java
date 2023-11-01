package com.project_group_5.telegram;
import com.project_group_5.Settings.Settings;
import com.project_group_5.currency.*;
import com.project_group_5.currencyUi.ShowCurr;
import com.project_group_5.telegram.commands.startCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.project_group_5.Settings.Settings.*;
import static com.project_group_5.Settings.TwoCurrencySettings.*;


public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {
    private final CurrencyServicePrivate currencyServicePrivate;
    private final CurrencyServiceMono currencyServiceMono;
    private final CurrencyServiceNBU currencyServiceNBU;
    private final ShowCurr showCurr;
    Long chatId;



    public CurrencyTelegramBot() {
        //Инициализация сервисов
        currencyServicePrivate = new PrivatBankCurrencyService();
        currencyServiceMono = new MonoBankCurrencyService();
        currencyServiceNBU = new NBUCurrencyService();
        showCurr = new ShowCurr();
        register(new startCommand());
    }

    @Override
    public String getBotUsername() {
        return BotConstant.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConstant.BOT_TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        String showCuText;
        Settings settings = new Settings();
        chatId = update.getCallbackQuery().getMessage().getChatId();
        String chatIdForMess = Long.toString(chatId);

        if (update.getCallbackQuery().getData().equals("getInfoButton")) {
            String result;
            try {
                result = implementSettings(chatId);
                startCurrencyUpdates(9);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                execute(showTextWithInitialButtons(chatId, result));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("settings")) {
            SendMessage settingsMess = new SendMessage();
            InlineKeyboardButton getNumber_of_signs = InlineKeyboardButton
                    .builder()
                    .text("Кількість знаків після коми")
                    .callbackData("Number_of_signs")
                    .build();
            InlineKeyboardButton getBank = InlineKeyboardButton
                    .builder()
                    .text("Банк")
                    .callbackData("Bank")
                    .build();
            InlineKeyboardButton getCurrency = InlineKeyboardButton
                    .builder()
                    .text("Валюти")
                    .callbackData("Currency")
                    .build();
            InlineKeyboardButton getNotific = InlineKeyboardButton
                    .builder()
                    .text("Час сповіщень")
                    .callbackData("Notific")
                    .build();

            List<InlineKeyboardButton> buttonsOfSettings = Stream.of(getNumber_of_signs, getBank, getCurrency, getNotific)
                    .map(it -> InlineKeyboardButton.builder().text(it.getText()).callbackData(it.getCallbackData()).build())
                    .collect(Collectors.toList());

            InlineKeyboardMarkup keyboardSettings = InlineKeyboardMarkup
                    .builder()
                    .keyboard(Collections.singleton(buttonsOfSettings))
                    .build();
            showCuText = "Налаштування";
            settingsMess.setText(showCuText);
            settingsMess.setReplyMarkup(keyboardSettings);
            settingsMess.setChatId(chatIdForMess);
            try {
                execute(settingsMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("Number_of_signs")) {
            SendMessage singsMess = new SendMessage();
            InlineKeyboardButton sings_2 = null;
            try {
                sings_2 = InlineKeyboardButton
                        .builder()
                        .text(getTextNumberOfSigns(chatId, "-2", "2"))
                        .callbackData("2_sings")
                        .build();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            InlineKeyboardButton sings_3 = null;
            try {
                sings_3 = InlineKeyboardButton
                        .builder()
                        .text(getTextNumberOfSigns(chatId, "-3", "3"))
                        .callbackData("3_sings")
                        .build();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            InlineKeyboardButton sings_4 = null;
            try {
                sings_4 = InlineKeyboardButton
                        .builder()
                        .text(getTextNumberOfSigns(chatId, "-4", "4"))
                        .callbackData("4_sings")
                        .build();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            List<InlineKeyboardButton> buttonsOfSings = Stream.of(sings_2, sings_3, sings_4)
                    .map(it -> InlineKeyboardButton.builder().text(it.getText()).callbackData(it.getCallbackData()).build())
                    .collect(Collectors.toList());

            InlineKeyboardMarkup keyboardSings = InlineKeyboardMarkup
                    .builder()
                    .keyboard(Collections.singleton(buttonsOfSings))
                    .build();
            showCuText = "Кількість знаків після коми";
            singsMess.setText(showCuText);
            singsMess.setReplyMarkup(keyboardSings);
            singsMess.setChatId(chatIdForMess);
            try {
                execute(singsMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("2_sings")) {
            try {
                settings.setSettings(chatId, "Number_of_signs", "2");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String text = "Кількість знаків встановлено: 2";
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("3_sings")) {
            try {
                settings.setSettings(chatId, "Number_of_signs", "3");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String text = "Кількість знаків встановлено: 3";
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("4_sings")) {
            try {
                settings.setSettings(chatId, "Number_of_signs", "4");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String text = "Кількість знаків встановлено: 4";
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("Currency")) {
            SendMessage currencyMess = new SendMessage();
            InlineKeyboardButton usd = null;
            try {
                usd = InlineKeyboardButton
                        .builder()
                        .text(getTextForUsd(chatId, "USD"))
                        .callbackData("USD")
                        .build();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            InlineKeyboardButton eur = null;
            try {
                eur = InlineKeyboardButton
                        .builder()
                        .text(getTextForEuro(chatId, "EUR"))
                        .callbackData("EUR")
                        .build();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            List<InlineKeyboardButton> buttonsOfSings = Stream.of(usd, eur)
                    .map(it -> InlineKeyboardButton.builder().text(it.getText()).callbackData(it.getCallbackData()).build())
                    .collect(Collectors.toList());

            InlineKeyboardMarkup keyboardCurrencies = InlineKeyboardMarkup
                    .builder()
                    .keyboard(Collections.singleton(buttonsOfSings))
                    .build();
            showCuText = "Валюта";
            currencyMess.setText(showCuText);
            currencyMess.setReplyMarkup(keyboardCurrencies);
            currencyMess.setChatId(chatIdForMess);
            try {
                execute(currencyMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }


//            List<InlineKeyboardButton> buttonsCurrency = Stream.of(CurrencyPrivate.EUR, CurrencyPrivate.USD)
//                    .map(Enum::name)
//                    .map(it -> InlineKeyboardButton.builder().text(it).callbackData(it).build())
//                    .collect(Collectors.toList());
//            InlineKeyboardMarkup keyboardCurrency = InlineKeyboardMarkup
//                    .builder()
//                    .keyboard(Collections.singleton(buttonsCurrency))
//                    .build();
//            showCuText = "Валюти";
//            currencyMess.setText(showCuText);
//            currencyMess.setReplyMarkup(keyboardCurrency);
//            currencyMess.setChatId(chatIdForMess);
//            try {
//                execute(currencyMess);
//            } catch (TelegramApiException e) {
//                throw new RuntimeException(e);
//            }

        if (update.getCallbackQuery().getData().equals("USD")) {
            String text = "";
            try {
                if (!isSettingsFile(chatId)) {
                    settings.setSettings(chatId, "Currency", "USD");
                    text = "Валюту встановлено: Долар США";
                } else if (isSettingsFile(chatId) && !isSettedTwoCurrencies(chatId)) {
                    if (isSettedCurrency(chatId, "USD")) {
                        text = "Долар США вже встановлено";
                    } else {
                        setSecondCurrency(chatId, "USD");
                        text = "Другу валюту додано: Долар США";
                    }
                } else if (isSettedTwoCurrencies(chatId)) {
                    removeOneCurrency(chatId, "USD");
                    text = "Залишилась одна валюта: Євро";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("EUR")) {
            String text = "";
            try {
                if (!isSettingsFile(chatId)) {
                    settings.setSettings(chatId, "Currency", "EUR");
                    text = "Валюту встановлено: Євро";
                } else if (isSettingsFile(chatId) && !isSettedTwoCurrencies(chatId)) {
                    if (isSettedCurrency(chatId, "EUR")) {
                        text = "Євро вже встановлено";
                    } else {
                        setSecondCurrency(chatId, "EUR");
                        text = "Другу валюту додано: Євро";
                    }
                } else if (isSettedTwoCurrencies(chatId)) {
                    removeOneCurrency(chatId, "EUR");
                    text = "Залишилась одна валюта: Долар США";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("Bank")) {
            SendMessage bankMess = new SendMessage();
            InlineKeyboardButton privat = null;
            try {
                privat = InlineKeyboardButton
                        .builder()
                        .text(getTextForBanks(chatId, "Приват Банк", "privat"))
                        .callbackData("privat")
                        .build();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            InlineKeyboardButton mono = null;
            try {
                mono = InlineKeyboardButton
                        .builder()
                        .text(getTextForBanks(chatId, "Моно Банк", "mono"))
                        .callbackData("mono")
                        .build();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            InlineKeyboardButton nbu = null;
            try {
                nbu = InlineKeyboardButton
                        .builder()
                        .text(getTextForBanks(chatId, "НБУ", "nbu"))
                        .callbackData("nbu")
                        .build();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            List<InlineKeyboardButton> buttonsOfBanks = Stream.of(privat, mono, nbu)
                    .map(it -> InlineKeyboardButton.builder().text(it.getText()).callbackData(it.getCallbackData()).build())
                    .collect(Collectors.toList());

            InlineKeyboardMarkup keyboardBanks = InlineKeyboardMarkup
                    .builder()
                    .keyboard(Collections.singleton(buttonsOfBanks))
                    .build();

            showCuText = "Банки";
            bankMess.setText(showCuText);
            bankMess.setReplyMarkup(keyboardBanks);
            bankMess.setChatId(chatIdForMess);
            try {
                execute(bankMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("privat")) {
            try {
                settings.setSettings(chatId, "Bank", "privat");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String text = "Банк встановлено: ПриватБанк";
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("mono")) {
            try {
                settings.setSettings(chatId, "Bank", "mono");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String text = "Банк встановлено: МоноБанк";
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("nbu")) {
            try {
                settings.setSettings(chatId, "Bank", "nbu");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String text = "Банк встановлено: Національний Банк України";
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("Notific")) {
            SendMessage notificMess = new SendMessage();

            InlineKeyboardButton notificationTime = InlineKeyboardButton
                    .builder()
                    .text("Час сповіщень")
                    .callbackData("NotificationTime")
                    .build();
            InlineKeyboardButton disableNotification = InlineKeyboardButton
                    .builder()
                    .text("Вимкнути сповіщення")
                    .callbackData("DisableNotification")
                    .build();
            List<InlineKeyboardButton> buttonsOfStart = Stream.of(notificationTime, disableNotification)
                    .map(it -> InlineKeyboardButton.builder().text(it.getText()).callbackData(it.getCallbackData()).build())
                    .collect(Collectors.toList());

//            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//            List<KeyboardRow> keyNotific = new ArrayList<>();
//
//            KeyboardRow row = new KeyboardRow();
//            row.add("9");
//            row.add("10");
//            row.add("11");
//            keyNotific.add(row);
//
//            row = new KeyboardRow();
//            row.add("12");
//            row.add("13");
//            row.add("14");
//            keyNotific.add(row);
//
//            row = new KeyboardRow();
//            row.add("15");
//            row.add("16");
//            row.add("17");
//            keyNotific.add(row);
//
//            row = new KeyboardRow();
//            row.add("18");
//            row.add("Вимкнути повідомлення");
//            keyNotific.add(row);
//
            InlineKeyboardMarkup keyboardSings = InlineKeyboardMarkup
                    .builder()
                    .keyboard(Collections.singleton(buttonsOfStart))
                    .build();

            showCuText = "Час сповіщень";
            notificMess.setText(showCuText);
            notificMess.setReplyMarkup(keyboardSings);
            notificMess.setChatId(chatIdForMess);
            try {
                execute(notificMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("NotificationTime")) {
            SendMessage currencyMess = new SendMessage();
            InlineKeyboardButton nine = InlineKeyboardButton
                        .builder()
                        .text("9")
                        .callbackData("9")
                        .build();
                        InlineKeyboardButton eur = null;
            InlineKeyboardButton ten = InlineKeyboardButton
                        .builder()
                        .text("10")
                        .callbackData("10")
                        .build();
            InlineKeyboardButton eleven = InlineKeyboardButton
                    .builder()
                    .text("11")
                    .callbackData("11")
                    .build();
            InlineKeyboardButton twelve = InlineKeyboardButton
                    .builder()
                    .text("12")
                    .callbackData("12")
                    .build();
            InlineKeyboardButton thr = InlineKeyboardButton
                    .builder()
                    .text("13")
                    .callbackData("13")
                    .build();
            InlineKeyboardButton four = InlineKeyboardButton
                    .builder()
                    .text("14")
                    .callbackData("14")
                    .build();
            InlineKeyboardButton fift = InlineKeyboardButton
                    .builder()
                    .text("15")
                    .callbackData("15")
                    .build();
            InlineKeyboardButton sixt = InlineKeyboardButton
                    .builder()
                    .text("16")
                    .callbackData("16")
                    .build();
            InlineKeyboardButton sevent = InlineKeyboardButton
                    .builder()
                    .text("17")
                    .callbackData("17")
                    .build();
            InlineKeyboardButton eight = InlineKeyboardButton
                    .builder()
                    .text("18")
                    .callbackData("18")
                    .build();


            List<InlineKeyboardButton> buttonsOfSings = Stream.of(nine, ten, eleven, twelve, thr, four, fift, sixt, sevent, eight)
                    .map(it -> InlineKeyboardButton.builder().text(it.getText()).callbackData(it.getCallbackData()).build())
                    .collect(Collectors.toList());

            InlineKeyboardMarkup keyboardCurrencies = InlineKeyboardMarkup
                    .builder()
                    .keyboard(Collections.singleton(buttonsOfSings))
                    .build();
            showCuText = "Час сповіщень";
            currencyMess.setText(showCuText);
            currencyMess.setReplyMarkup(keyboardCurrencies);
            currencyMess.setChatId(chatIdForMess);
            try {
                execute(currencyMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }


        if (update.getCallbackQuery().getData().equals("9")) {
            String text = "Час сповіщень встановлено: 9 годин";
            startCurrencyUpdates(9);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("10")) {
            String text = "Час сповіщень встановлено: 10 годин";
            startCurrencyUpdates(10);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("11")) {
            String text = "Час сповіщень встановлено: 11 годин";
            startCurrencyUpdates(11);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("12")) {
            String text = "Час сповіщень встановлено: 12 годин";
            startCurrencyUpdates(12);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("13")) {
            String text = "Час сповіщень встановлено: 13 годин";
            startCurrencyUpdates(13);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("14")) {
            String text = "Час сповіщень встановлено: 14 годин";
            startCurrencyUpdates(14);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("15")) {
            String text = "Час сповіщень встановлено: 15 годин";
            startCurrencyUpdates(15);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("16")) {
            String text = "Час сповіщень встановлено: 16 годин";
            startCurrencyUpdates(16);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("17")) {
            String text = "Час сповіщень встановлено: 17 годин";
            startCurrencyUpdates(17);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        if (update.getCallbackQuery().getData().equals("18")) {
            String text = "Час сповіщень встановлено: 18 годин";
            startCurrencyUpdates(18);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        if (update.getCallbackQuery().getData().equals("11")) {
            String text = "Час сповіщень встановлено: 11 годин";
            startCurrencyUpdates(11);
            try {
                execute(showTextWithInitialButtons(chatId, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("DisableNotification")) {
            SendMessage disableNotificationMess = new SendMessage();
            startCurrencyUpdates(0);
            String text = "Сповіщення вимкнуті";
            disableNotificationMess.setText(text);
            disableNotificationMess.setChatId(chatIdForMess);
            try {
                execute(disableNotificationMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void startCurrencyUpdates(long notificationHour) {
        Timer timer = new Timer();
//        int notificationHour = 9; // Установка времени рассылки (9:00 утра)
        long interval = 60 * 60 * 1000; // Интервал рассылки (1 час)
        Date firstExecutionTime = calculateFirstExecutionTime(notificationHour);
        if (notificationHour != 0) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // В этом методе получаем актуальные курсы и отправляем их подписчикам
                    try {
                        sendCurrencyUpdateToSubscribers();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, firstExecutionTime, interval);
        }else timer.cancel();
    }



    private Date calculateFirstExecutionTime(long notificationHour) {
        long currentTime = System.currentTimeMillis();
        long todayNotificationTime = currentTime - (currentTime % 86400000) + (notificationHour * 3600000L); // 86400000 миллисекунд в сутках, 3600000 миллисекунд в часе
        if (todayNotificationTime <= currentTime) {
            todayNotificationTime += 86400000; // Если указанное время уже прошло сегодня, переносим на завтра
        }
        return new Date(todayNotificationTime);
    }

    private void sendCurrencyUpdateToSubscribers() throws IOException {
//        CurrencyPrivate currency = CurrencyPrivate.USD;
//        double exchangeRate = currencyServicePrivate.getRatePrivate(currency);//Выбор банка
        // Отправка сообщения с курсом валюты подписчикам
        SendMessage message = new SendMessage();
//        message.setChatId(chatId.toString());
        message.setText(implementSettings(chatId));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendMessage showTextWithInitialButtons(long chatId, String text) {
        SendMessage message = new SendMessage();
        InlineKeyboardButton getInfoButton = InlineKeyboardButton
                .builder()
                .text("Отримати інфо")
                .callbackData("getInfoButton")
                .build();

        InlineKeyboardButton settings = InlineKeyboardButton
                .builder()
                .text("Налаштування")
                .callbackData("settings")
                .build();

        List<InlineKeyboardButton> InitialButtons = Stream.of(getInfoButton, settings)
                .map(it -> InlineKeyboardButton.builder().text(it.getText()).callbackData(it.getCallbackData()).build())
                .collect(Collectors.toList());

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup
                .builder()
                .keyboard(Collections.singleton(InitialButtons))
                .build();
        message.setText(text);
        message.setReplyMarkup(keyboard);
        message.setChatId(Long.toString(chatId));
        return message;
    }
}
