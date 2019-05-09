package io.lenses.alerting.plugin;

import io.lenses.alerting.plugin.util.Try;

import java.util.Map;

interface AlertingPlugin {
    /**
     * Returns AlertingService for valid configuration (depends on the alerting service class).
     * @param config service configuration
     * @return alerting service or failure
     */
    Try<AlertingService> init(Map<String, String> config);
}