package com.xcodeassociated.cloud.event.bootstrap;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Application bootstrap starting");
    }
}