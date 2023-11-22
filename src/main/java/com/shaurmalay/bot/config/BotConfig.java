package com.shaurmalay.bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Vladislav Tugulev
 * @Date 23.10.2023
 */

@Configuration
@PropertySource("application.properties")
@Data
public class BotConfig {
    @Value("${BOT_TOKEN}")
    private String token;
    @Value("${BOT_NAME")
    private String botName;
    @Value("${deliveryChatId}")
    private Long delChatId;
    @Value("${TERMINAL_ID}")
    private Long terminalId;

    @Value("${TERMINAL_PASSWORD}")
    private String terminalPass;

    @Value("${URL_INIT}")
    private String urlInit;
    @Value("${URL_GETSTATE}")
    private String urlGetState;
    @Value("${URL_CANCEL}")
    private String urlCancel;
}
