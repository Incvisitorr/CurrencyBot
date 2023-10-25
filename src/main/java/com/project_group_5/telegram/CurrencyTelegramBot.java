package com.project_group_5.telegram;

import com.project_group_5.currency.Currency;
import com.project_group_5.currency.CurrencyService;
import com.project_group_5.currency.PrivatBankCurrencyService;
import com.project_group_5.currencyUi.ShowCurr;
import com.project_group_5.telegram.commands.startCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage respMess = new SendMessage();

        if (update.getCallbackQuery().getData().equals("getInfoButton")) {
            String callbackQuery = update.getCallbackQuery().getData();

            Currency currency = Currency.valueOf(Currency.USD.name());//Заглушка для настроек по умолчанию
            double currancyReate = currencyService.getRate(currency);
            String showCuText = showCurr.convert(currancyReate, currency);
            respMess.setText(showCuText);

            respMess.setChatId(Long.toString(chatId));
            try {
                execute(respMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            //System.out.println(callbackQuery);
        }

        if (update.getCallbackQuery().getData().equals("settings")) {
            String callbackQuery = update.getCallbackQuery().getData();
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
            List<InlineKeyboardButton> buttonsOfSettings = Stream.of(getNumber_of_signs,getBank,getCurrency, getNotific)
                    .map(it -> InlineKeyboardButton.builder().text(it.getText()).callbackData(it.getCallbackData()).build())
                    .collect(Collectors.toList());

            InlineKeyboardMarkup keyboardSettings = InlineKeyboardMarkup
                    .builder()
                    .keyboard(Collections.singleton(buttonsOfSettings))
                    .build();
            System.out.println(callbackQuery);

            respMess.setChatId(Long.toString(chatId));
            respMess.setReplyMarkup(keyboardSettings);
            try {
                execute(respMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            System.out.println(callbackQuery);
        }

    }

}

