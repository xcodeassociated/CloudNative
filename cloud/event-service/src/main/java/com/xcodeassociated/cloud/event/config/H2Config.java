package com.xcodeassociated.cloud.event.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Profile("dev")
public class H2Config {
    private org.h2.tools.Server webServer;
    private org.h2.tools.Server server;

    @Autowired
    ConfigStore configStore;

    @EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
    public void start() throws java.sql.SQLException {
        log.debug("ConfigStore instance: " + configStore);

        log.debug("H2 console starting...");
        this.webServer = org.h2.tools.Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start();
        this.server = org.h2.tools.Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    @EventListener(org.springframework.context.event.ContextClosedEvent.class)
    public void stop() {
        log.debug("H2 console closing...");
        this.webServer.stop();
        this.server.stop();
    }

}
