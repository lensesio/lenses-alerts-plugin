/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.javaapi;

import io.lenses.alerting.plugin.javaapi.util.Try;

import java.util.Map;

public interface AlertingPlugin {
    /**
     * Returns AlertingService for valid configuration (depends on the alerting service class).
     * @param config service configuration
     * @return alerting service or failure
     */
    Try<AlertingService> init(Map<String, String> config);
}