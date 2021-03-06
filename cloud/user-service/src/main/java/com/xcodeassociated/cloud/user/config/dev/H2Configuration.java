package com.xcodeassociated.cloud.user.config.dev;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Profile({"dev", "local-dev"})
@Configuration
public class H2Configuration {
    @Bean
    @Scope(value = "singleton")
    public H2Env getH2ConfigStore() {
        return H2Env.getInstance();
    }
}
