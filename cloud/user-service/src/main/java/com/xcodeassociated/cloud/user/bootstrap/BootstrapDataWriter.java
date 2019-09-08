package com.xcodeassociated.cloud.user.bootstrap;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Profile("dev")
public class BootstrapDataWriter implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        log.info("DataWriter Bean Context Started");
    }
}
