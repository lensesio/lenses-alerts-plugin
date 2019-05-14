/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin;

import java.util.Map;
import java.util.Optional;

public interface Alert {
    Map<String, String> labels();
    Map<String, String> annotations();
    String startsAt();
    Optional<String> endsAt();
    String generatorURL();
    int alertId();
}
