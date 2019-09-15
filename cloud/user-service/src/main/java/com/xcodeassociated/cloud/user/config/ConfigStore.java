package com.xcodeassociated.cloud.user.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class ConfigStore {
    private static ConfigStore INSTANCE = null;
    @Value("${com.xcodeassociated.config.user.h2.web.port}")
    private String h2WebPort;
    @Value("${com.xcodeassociated.config.user.h2.web.flag}")
    private String h2WebFlag;
    @Value("${com.xcodeassociated.config.user.h2.tcp.port}")
    private String h2TcpPort;
    @Value("${com.xcodeassociated.config.user.h2.tcp.flag}")
    private String h2TcpFlag;

    private ConfigStore() {}

    static ConfigStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigStore();
        }
        return INSTANCE;
    }
}
