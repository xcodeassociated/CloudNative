package com.xcodeassociated.cloud.event.config.dev;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Getter
public class H2Env {
    private static H2Env INSTANCE = null;

    @Value("${com.xcodeassociated.config.user.h2.web.port}")
    private String h2WebPort;
    @Value("${com.xcodeassociated.config.user.h2.web.flag}")
    private String h2WebFlag;
    @Value("${com.xcodeassociated.config.user.h2.tcp.port}")
    private String h2TcpPort;
    @Value("${com.xcodeassociated.config.user.h2.tcp.flag}")
    private String h2TcpFlag;

    private H2Env() {}

    static H2Env getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new H2Env();
        }
        return INSTANCE;
    }
}
