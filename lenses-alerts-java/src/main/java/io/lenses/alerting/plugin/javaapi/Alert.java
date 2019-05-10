/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.javaapi;

import java.util.Map;
import java.util.Optional;

final public class Alert implements io.lenses.alerting.plugin.Alert {
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

    @Override
    public Map<String, String> labels() {
        return labels;
    }

    @Override
    public Map<String, String> annotations() {
        return annotations;
    }

    @Override
    public String startsAt() {
        return startsAt;
    }

    @Override
    public Optional<String> endsAt() {
        return endsAt.empty();
    }

    @Override
    public String generatorURL() {
        return generatorURL;
    }

    @Override
    public int alertId() {
        return alertId;
    }
}
