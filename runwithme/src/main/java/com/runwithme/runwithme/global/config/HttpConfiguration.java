package com.runwithme.runwithme.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.runwithme.runwithme.global.webhook.NotificationManager;
import com.runwithme.runwithme.global.webhook.NotificationSender;
import com.runwithme.runwithme.global.webhook.discord.DiscordSender;

@Configuration
public class HttpConfiguration {

    @Bean
    public NotificationSender notificationSender() {
        return new DiscordSender();
    }

    @Bean
    public NotificationManager notificationManager() {
        return new NotificationManager(notificationSender());
    }
}
