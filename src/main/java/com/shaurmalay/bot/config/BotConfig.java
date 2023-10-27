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
}
