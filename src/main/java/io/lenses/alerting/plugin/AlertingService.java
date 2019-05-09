package io.lenses.alerting.plugin;

import io.lenses.alerting.plugin.util.Try;

public interface AlertingService {
    /**
     * Publishes event to alerting service.
     * @param alert alert to be published
     * @return nothing if successful, failure if not
     */
    Try<Void> publish(Alert alert);
}
