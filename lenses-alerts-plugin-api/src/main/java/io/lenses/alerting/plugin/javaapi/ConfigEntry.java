package io.lenses.alerting.plugin.javaapi;

public class ConfigEntry {
    private final String key;
    private final String description;

    public ConfigEntry(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
