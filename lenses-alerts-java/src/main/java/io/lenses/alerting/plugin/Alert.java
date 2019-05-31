/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin;

import java.util.Optional;

public class Alert {

    public Alert(AlertLevel level,
                 String category,
                 Iterable<String> tags,
                 String instance,
                 String summary,
                 Optional<String> docs,
                 int alertId) {
        this.level = level;
        this.category = category;
        this.tags = tags;
        this.instance = instance;
        this.summary = summary;
        this.timestamp = System.currentTimeMillis();
        this.docs = docs;
        this.alertId = alertId;
    }

    /**
     * The importance of the alert
     */
    public final AlertLevel level;

    /**
     * Category for the alert
     */
    public final String category;

    /**
     * Allows the user to configure some routing information
     */
    public final Iterable<String> tags;

    /**
     * Contains the lenses instance raising the alert
     */
    public final String instance;

    /**
     * Returns the information for this alert. For example: event x has occurred and you need to do this.
     */
    public final String summary;

    /**
     * The epoch time when this alert was created
     */
    public final Long timestamp;

    /**
     * Provides documentation information related to the alert.
     */
    public final Optional<String> docs;

    /**
     * Returns a unique identifier for the alert type
     */
    public final int alertId;
}
