package com.DTISE.ShelfMasterBE.infrastructure.payment.config;

//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//
//@Data
//@Configuration
//@ConfigurationProperties(prefix = "midtrans")
//public class MidtransConfigProperties {
//
//    private String serverKey;
//    private String clientKey;
//    private boolean isProduction;
//}

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "midtrans")
public class MidtransConfigProperties {
    private String serverKey;
    private String clientKey;
    private boolean isProduction;
}

