package com.xcodeassociated.cloud.event.config;

    import lombok.Getter;
    import org.springframework.beans.factory.annotation.Value;

@Getter
public class ConfigStore {
    private static ConfigStore INSTANCE = null;
    @Value("${com.xcodeassociated.config.event.example}")
    private String example;

    private ConfigStore() {}

    static ConfigStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigStore();
        }
        return INSTANCE;
    }
}
