/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.javaapi.util;

public class Failure<T> implements Try<T> {
    public final Throwable exception;

    public Failure(Throwable exception) {
        this.exception = exception;
    }
}
