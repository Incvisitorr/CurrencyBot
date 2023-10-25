package com.project_group_5.telegram.commands;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class startCommand extends BotCommand {
    String userName;
    Long chatId;
    String chatIdForMess;
    SendMessage msg;

    public startCommand() {
        super("start", "Start Command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        msg = new SendMessage();
        chatId = chat.getId();
        userName = user.getFirstName();
        chatIdForMess = Long.toString(chatId);

        String text = "Ласкаво просимо " + userName + ".\nЦей бот допоможе відслідковувати актуальні курси валют";
        msg.setText(text);
        msg.setChatId(chatIdForMess);

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

        List<InlineKeyboardButton> buttonsOfStart = Stream.of(getInfoButton, settings)
                .map(it -> InlineKeyboardButton.builder().text(it.getText()).callbackData(it.getCallbackData()).build())
                .collect(Collectors.toList());

        InlineKeyboardMarkup keyboardStart = InlineKeyboardMarkup
                .builder()
                .keyboard(Collections.singleton(buttonsOfStart))
                .build();


        msg.setReplyMarkup(keyboardStart);

        try {
            absSender.execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
