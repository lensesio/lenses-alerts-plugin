package io.lenses.alerting.plugin;

import java.util.Map;
import java.util.Optional;

public class Alert {
    public final Map<String, String> labels;
    public final Map<String, String> annotations;
    public final String startsAt;
    public final Optional<String> endsAt;
    public final String generatorURL;
    public final int alertId;

    public Alert(Map<String, String> labels, Map<String, String> annotations, String startsAt, Optional<String> endsAt,
                 String generatorURL, int alertId) {
        this.labels = labels;
        this.annotations = annotations;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.generatorURL = generatorURL;
        this.alertId = alertId;
    }
}
