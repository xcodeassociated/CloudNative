package com.xcodeassociated.cloud.user.config.dev;

import com.xcodeassociated.cloud.user.config.ConfigStore;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Profile({"dev", "local-dev"})
public class H2Init {
    private org.h2.tools.Server webServer;
    private org.h2.tools.Server server;

    @Autowired
    H2Env configStore;

    @EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
    public void start() throws java.sql.SQLException {
        log.debug("ConfigStore instance: " + configStore);
        log.debug("H2 console starting...");

        this.webServer = org.h2.tools.Server.createWebServer(
            "-webPort",
            this.configStore.getH2WebPort(),
            this.configStore.getH2WebFlag())
            .start();

        log.debug("Starting H2 Web server: " + this.configStore.getH2WebPort());

        this.server = org.h2.tools.Server.createTcpServer(
            "-tcpPort",
            this.configStore.getH2TcpPort(),
            this.configStore.getH2TcpFlag())
            .start();

        log.debug("Starting H2 Tcp server: " + this.configStore.getH2TcpPort());
    }

    @EventListener(org.springframework.context.event.ContextClosedEvent.class)
    public void stop() {
        log.debug("H2 console closing...");
        this.webServer.stop();
        this.server.stop();
    }

}
