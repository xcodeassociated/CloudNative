package com.xcodeassociated.cloud.user.bootstrap;

import com.xcodeassociated.cloud.user.config.ConfigStore;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ApplicationBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    ConfigStore configStore;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Application bootstrap starting");
        log.debug("ConfigStore referance: " + configStore);
    }
}
