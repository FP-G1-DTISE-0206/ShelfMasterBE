package com.DTISE.ShelfMasterBE.infrastructure.payment.config;

import com.midtrans.Config;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

@Component // ✅ Change from @Configuration to @Component
@Getter
public class MidtransConfig {

    @Value("${midtrans.server-key}")
    private String serverKey;

    @Value("${midtrans.client-key}")
    private String clientKey;

    @Value("${midtrans.is-production}")
    private boolean isProduction;

    private Config config;

    @PostConstruct
    public void init() {
        this.config = new Config(serverKey, clientKey, isProduction); // ✅ Manually initialize Config
    }

    public Config getConfig() {
        return config;
    }
}
