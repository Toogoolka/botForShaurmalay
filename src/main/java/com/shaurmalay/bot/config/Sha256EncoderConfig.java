package com.shaurmalay.bot.config;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Sha256EncoderConfig {
    @Bean
    public Sha256Encoder sha256Encoder() {
        return new Sha256Encoder();
    }
    public static class Sha256Encoder {
        public String encode(String input) {
            return DigestUtils.sha256Hex(input);
        }
    }
}
