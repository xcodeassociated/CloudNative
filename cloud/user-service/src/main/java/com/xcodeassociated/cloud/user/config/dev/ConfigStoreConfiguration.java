package com.xcodeassociated.cloud.user.config.dev;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Profile("dev")
@Configuration
public class ConfigStoreConfiguration {

    @Bean
    @Scope(value = "singleton")
    ConfigStore getConfigStore() {
        return ConfigStore.getInstance();
    }

}
