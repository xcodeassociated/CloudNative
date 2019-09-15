package com.xcodeassociated.cloud.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ConfigStoreConfiguration {

    @Bean
    @Scope(value = "singleton")
    ConfigStore getConfigStore() {
        return ConfigStore.getInstance();
    }

}
