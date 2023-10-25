package com.project_group_5.telegram;

import com.project_group_5.currency.Currency;
import com.project_group_5.currency.CurrencyService;
import com.project_group_5.currency.PrivatBankCurrencyService;
import com.project_group_5.currencyUi.ShowCurr;
import com.project_group_5.telegram.commands.startCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {
    private CurrencyService currencyService;
    private ShowCurr showCurr;

    public CurrencyTelegramBot() {
        //Инициализация сервисов
        currencyService = new PrivatBankCurrencyService();
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
//        if(update.hasMessage()){
//            String msg=update.getMessage().getText();
//            String respText="You are writing "+msg;
//            SendMessage sendMessage=new SendMessage();
//            sendMessage.setText(respText);
//            sendMessage.setChatId(Long.toString(update.getMessage().getChatId()));
//            try {
//                execute(sendMessage);
//            } catch (TelegramApiException e) {
//                throw new RuntimeException(e);
//            }
//        }

        if (update.getCallbackQuery().getData().equals("getInfoButton")) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            SendMessage respMess = new SendMessage();

            Currency currency = Currency.valueOf(Currency.USD.name());//Заглушка для настроек по умолчанию
            double currancyReate = currencyService.getRate(currency);
            showCuText = showCurr.convert(currancyReate, currency);

            respMess.setText(showCuText);
            respMess.setChatId(Long.toString(chatId));
            try {
                execute(respMess);
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
                    .text("Час оповіщень")
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
            settingsMess.setChatId(Long.toString(update.getCallbackQuery().getMessage().getChatId()));
            try {
                execute(settingsMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }

        if (update.getCallbackQuery().getData().equals("Number_of_signs")) {
            SendMessage singsMess = new SendMessage();
            InlineKeyboardButton sings_2 = InlineKeyboardButton
                    .builder()
                    .text("-2")
                    .callbackData("2_sings")
                    .build();
            InlineKeyboardButton sings_3 = InlineKeyboardButton
                    .builder()
                    .text("-3")
                    .callbackData("3_sings")
                    .build();
            InlineKeyboardButton sings_4 = InlineKeyboardButton
                    .builder()
                    .text("-4")
                    .callbackData("4_sings")
                    .build();
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
            singsMess.setChatId(Long.toString(update.getCallbackQuery().getMessage().getChatId()));
            try {
                execute(singsMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (update.getCallbackQuery().getData().equals("Currency")) {
            SendMessage currencyMess = new SendMessage();
            List<InlineKeyboardButton> buttonsCurrency = Stream.of(Currency.EUR, Currency.USD)
                    .map(Enum::name)
                    .map(it -> InlineKeyboardButton.builder().text(it).callbackData(it).build())
                    .collect(Collectors.toList());
            InlineKeyboardMarkup keyboardCurrency = InlineKeyboardMarkup
                    .builder()
                    .keyboard(Collections.singleton(buttonsCurrency))
                    .build();
            showCuText = "Валюти";
            currencyMess.setText(showCuText);
            currencyMess.setReplyMarkup(keyboardCurrency);
            currencyMess.setChatId(Long.toString(update.getCallbackQuery().getMessage().getChatId()));
            try {
                execute(currencyMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        if (update.getCallbackQuery().getData().equals("Bank")) {
            SendMessage bankMess = new SendMessage();
            InlineKeyboardButton privat = InlineKeyboardButton
                    .builder()
                    .text("Приват Банк")
                    .callbackData("privat")
                    .build();
            InlineKeyboardButton mono = InlineKeyboardButton
                    .builder()
                    .text("Моно Банк")
                    .callbackData("mono")
                    .build();
            InlineKeyboardButton nbu = InlineKeyboardButton
                    .builder()
                    .text("НБУ")
                    .callbackData("nbu")
                    .build();
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
            bankMess.setChatId(Long.toString(update.getCallbackQuery().getMessage().getChatId()));
            try {
                execute(bankMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }
        if (update.getCallbackQuery().getData().equals("Notific")) {

        }


    }

}

