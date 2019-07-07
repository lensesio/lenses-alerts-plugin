/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.javaapi;

import io.lenses.alerting.plugin.Alert;
import io.lenses.alerting.plugin.javaapi.util.Try;

import java.util.Map;

public interface AlertingService {
    /**
     * Provides the name for the alert integration service. For Slack integration this will read Slack.
     * @return The alert integration name
     */
    String name();

    /**
     * Provides information on what and how this alert integration service works.
     * @return
     */
    String description();


    /**
     * Returns a collection of label + value which represents the details about this alert integration service
     * to be displayed on the screen
     * @return
     */
    Map<String, String> displayedInformation();

    /**
     * Publishes event to alerting service.
     * @param alert alert to be published
     * @return published alert if successful, failure if not
     */
    Try<Alert> publish(Alert alert);
}
