package com.xcodeassociated.cloud.event.config;

import org.springframework.beans.factory.annotation.Value;

public class ConfigStore {
    private static ConfigStore INSTANCE = null;
    @Value("${com.xcodeassociated.config.h2.web.port}")
    private String h2WebPort;
    @Value("${com.xcodeassociated.config.h2.web.flag}")
    private String h2WebFlag;
    @Value("${com.xcodeassociated.config.h2.tcp.port}")
    private String h2TcpPort;
    @Value("${com.xcodeassociated.config.h2.tcp.flag}")
    private String h2TcpFlag;

    private ConfigStore() {}

    static ConfigStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigStore();
        }
        return INSTANCE;
    }

    public String getH2WebPort() {
        return h2WebPort;
    }

    public String getH2WebFlag() {
        return h2WebFlag;
    }

    public String getH2TcpPort() {
        return h2TcpPort;
    }

    public String getH2TcpFlag() {
        return h2TcpFlag;
    }
}
