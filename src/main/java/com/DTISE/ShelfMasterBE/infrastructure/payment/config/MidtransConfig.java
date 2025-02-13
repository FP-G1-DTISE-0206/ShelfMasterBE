package com.DTISE.ShelfMasterBE.infrastructure.payment.config;

import com.midtrans.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MidtransConfig {

    @Value("${midtrans.server-key}")
    private String serverKey;
    @Value("${midtrans.is-production}")
    private boolean isProduction;

    @Bean(name = "midtransConfigBean")
    public Config midtransConfig() {
        return Config.builder()
                .setServerKey(serverKey)
                .setIsProduction(isProduction)
                .build();
    }
}
