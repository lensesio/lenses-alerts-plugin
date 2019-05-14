/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.javaapi;

import io.lenses.alerting.plugin.Alert;
import io.lenses.alerting.plugin.javaapi.util.Try;

public interface AlertingService {
    /**
     * Publishes event to alerting service.
     * @param alert alert to be published
     * @return published alert if successful, failure if not
     */
    <T extends Alert> Try<T> publish(T alert);
}
