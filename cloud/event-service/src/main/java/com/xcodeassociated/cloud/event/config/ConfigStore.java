package com.xcodeassociated.cloud.event.config;

public class ConfigStore {
    private static ConfigStore INSTANCE = null;

    private ConfigStore() {}

    static ConfigStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigStore();
        }
        return INSTANCE;
    }

}
