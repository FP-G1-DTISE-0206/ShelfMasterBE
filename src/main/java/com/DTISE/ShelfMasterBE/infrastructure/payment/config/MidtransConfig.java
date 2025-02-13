package com.DTISE.ShelfMasterBE.infrastructure.payment.config;

import com.midtrans.Midtrans;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MidtransConfig {

    private final MidtransConfigProperties properties;

    @Bean
    public void configureMidtrans() {
        Midtrans.serverKey = properties.getServerKey();
        Midtrans.clientKey = properties.getClientKey();
        Midtrans.isProduction = properties.isProduction();
    }
}
